package io.github.warnotte.waxlib3.core.Profiler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe utilitaire pour mesurer le temps d'exécution de blocs de code.
 * Cette version construit une hiérarchie (arborescence) des mesures imbriquées.
 */
public class Tiktak {

	
	protected static final Logger	logger			= LogManager.getLogger(Tiktak.class);
	
    /**
     * Classe interne utilisée pour stocker une mesure en cours avec ses enfants.
     */
    private static class Measurement {
        final long startTime;
        final String event;
        final String threadName;
        // Liste des mesures enfants (qui seront converties en FinishedMeasurement)
        final List<FinishedMeasurement> children = new ArrayList<>();

        Measurement(long startTime, String event, String threadName) {
            this.startTime = startTime;
            this.event = event;
            this.threadName = threadName;
        }
    }

    /**
     * Classe représentant une mesure terminée, avec une arborescence de mesures enfants.
     */
    public static class FinishedMeasurement {
        private final String event;
        private final String threadName;
        private final long duration; // en millisecondes
        private final List<FinishedMeasurement> children;

        public FinishedMeasurement(String event, String threadName, long duration, List<FinishedMeasurement> children) {
            this.event = event;
            this.threadName = threadName;
            this.duration = duration;
            this.children = children;
        }

        public String getEvent() {
            return event;
        }

        public String getThreadName() {
            return threadName;
        }

        public long getDuration() {
            return duration;
        }

        public List<FinishedMeasurement> getChildren() {
            return children;
        }

        /**
         * Retourne une représentation formatée de la mesure (sans afficher les enfants).
         */
        @Override
        public String toString() {
            return String.format("Event: %-60s | Thread: %-15s | Duration: %-8d ms", 
                                 event.trim(), threadName.trim(), duration);
        }
    }

    // Chaque thread possède sa propre pile de mesures en cours.
    private static final ThreadLocal<Deque<Measurement>> measurementStack =
        ThreadLocal.withInitial(ArrayDeque::new);

    // Liste synchronisée pour stocker les mesures terminées (racines) pour analyse.
    private static final List<FinishedMeasurement> finishedMeasurements =
        Collections.synchronizedList(new ArrayList<>());

    /**
     * Démarre une mesure sans événement initial.
     */
    public static void tick() {
        tick(null);
    }

    /**
     * Démarre une mesure en associant dès le départ un événement.
     *
     * @param event L'information décrivant l'opération démarrée.
     */
    public static void tick(String event) {
        String threadName = Thread.currentThread().getName();
        measurementStack.get().push(new Measurement(System.currentTimeMillis(), event, threadName));
    }

    /**
     * Termine la dernière mesure sans information complémentaire.
     */
    public static void tack() {
        tack(null);
    }

    /**
     * Termine la dernière mesure en combinant l'information initiale et une éventuelle information complémentaire.
     *
     * @param eventOverride Information complémentaire à associer à la mesure terminée.
     */
    public static void tack(String eventOverride) {
        Deque<Measurement> stack = measurementStack.get();
        if (stack.isEmpty()) {
        	logger.error("Tack appelé sans tick correspondant.");
            return;
        }
        Measurement m = stack.pop();
        long duration = System.currentTimeMillis() - m.startTime;

        // Combine l'événement initial et l'override, s'ils sont définis.
        String eventInfo = m.event;
        if (eventOverride != null && !eventOverride.isEmpty()) {
            eventInfo = (eventInfo != null && !eventInfo.isEmpty() ? eventInfo + " | " : "") + eventOverride;
        }
        if (eventInfo == null || eventInfo.isEmpty()) {
            eventInfo = "Mesure";
        }

        // Crée la mesure terminée avec les enfants accumulés.
        FinishedMeasurement fm = new FinishedMeasurement(eventInfo, m.threadName, duration, new ArrayList<>(m.children));
        if (!stack.isEmpty()) {
            // Si une mesure parente est en cours, ajoute la mesure terminée en tant qu'enfant.
            Measurement parent = stack.peek();
            parent.children.add(fm);
        } else {
            // Sinon, c'est une mesure racine, on l'ajoute à la liste globale.
            finishedMeasurements.add(fm);
        }
        logger.info("Time (" + eventInfo + ") sur le thread [" + m.threadName + "]: " + duration + "ms");
    }

    /**
     * Retourne une copie de la liste des mesures terminées (racines).
     *
     * @return la liste des FinishedMeasurement enregistrés.
     */
    public static List<FinishedMeasurement> getFinishedMeasurements() {
        synchronized (finishedMeasurements) {
            return new ArrayList<>(finishedMeasurements);
        }
    }

    /**
     * Réinitialise toutes les mesures enregistrées.
     */
    public static void reset() {
        synchronized (finishedMeasurements) {
            finishedMeasurements.clear();
        }
        measurementStack.get().clear();
        logger.info("Les mesures ont été réinitialisées.");
    }

    /**
     * Affiche l'analyse globale et une vue arborescente des mesures.
     */
    public static void analyzeMeasurements() {
        List<FinishedMeasurement> roots;
        synchronized (finishedMeasurements) {
            roots = new ArrayList<>(finishedMeasurements);
        }
        if (roots.isEmpty()) {
        	logger.info("Aucune mesure enregistrée.");
            return;
        }
        long globalTotal = 0;
        for (FinishedMeasurement fm : roots) {
            globalTotal += fm.getDuration();
        }
        
    //    roots.sort(Comparator.comparingLong(FinishedMeasurement::getDuration).reversed());

        logger.info("===== Analyse Globale =====");
        logger.info("Nombre de mesures racines : " + roots.size());
        logger.info("Durée totale : " + globalTotal + "ms");

        
        // Affichage arborescent
        logger.info("----- Arborescence des mesures -----");
        for (FinishedMeasurement fm : roots) {
            displayMeasurementTree(fm, 0, globalTotal);
        }
    }

    /**
     * Affiche récursivement une mesure et ses enfants avec indentation,
     * en affichant le pourcentage en début de ligne.
     *
     * @param fm          La mesure terminée à afficher.
     * @param indentLevel Le niveau d'indentation (0 pour la racine).
     * @param globalTotal La durée totale de toutes les mesures racines pour calculer un pourcentage.
     */
    private static void displayMeasurementTree(FinishedMeasurement fm, int indentLevel, long globalTotal) {
//        String indent = "  ".repeat(indentLevel);
        String indent = (indentLevel == 0 ? "" : "  ".repeat(indentLevel - 1) + "\\-");

        
        
        double percentage = (globalTotal > 0 ? (double) fm.getDuration() / globalTotal * 100 : 0);
        // Affichage : le pourcentage sur 6.2f suivi du symbole %, puis l'indentation, puis l'info
        String line = String.format(" %-10.10s | %7d ms | %6.2f%%  | %s%-50.60s", 
        				fm.getThreadName() == null ? "" : fm.getThreadName(),
        				fm.getDuration(),
                        percentage,
                        indent,
                        fm.getEvent() == null ? "" : fm.getEvent())
                        ;
        logger.info(line);
/*
        for (FinishedMeasurement child : fm.getChildren()) {
            displayMeasurementTree(child, indentLevel + 1, globalTotal);
        }
        */
        /*
        List<FinishedMeasurement> sortedChildren = new ArrayList<>(fm.getChildren());
        sortedChildren.sort(Comparator.comparingLong(FinishedMeasurement::getDuration).reversed());
        for (FinishedMeasurement child : sortedChildren) {
            displayMeasurementTree(child, indentLevel + 1, globalTotal);
        }
*/
        
        List<FinishedMeasurement> sortedChildren = new ArrayList<>(fm.getChildren());
        sortedChildren.sort(Comparator.comparingLong(FinishedMeasurement::getDuration).reversed());
        for (FinishedMeasurement child : sortedChildren) {
            displayMeasurementTree(child, indentLevel + 1, globalTotal);
        }
     

    }
}
