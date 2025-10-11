# Script pour ajouter Maven au PATH
$mavenPath = "C:\apache-maven-3.8.8\bin"
$userPath = [Environment]::GetEnvironmentVariable("Path", "User")

if ($userPath -notlike "*$mavenPath*") {
    $newPath = $userPath + ";" + $mavenPath
    [Environment]::SetEnvironmentVariable("Path", $newPath, "User")
    Write-Host "✓ Maven ajouté au PATH utilisateur: $mavenPath"
    Write-Host ""
    Write-Host "IMPORTANT: Fermez et rouvrez votre terminal pour que les changements prennent effet."
} else {
    Write-Host "Maven est déjà dans le PATH utilisateur."
}
