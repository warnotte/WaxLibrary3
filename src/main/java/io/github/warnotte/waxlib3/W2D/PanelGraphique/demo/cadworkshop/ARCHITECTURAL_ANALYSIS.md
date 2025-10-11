# ARCHITECTURAL ANALYSIS - W2D Text Rendering

## Investigation Date: 2025-10-11

## THE ROOT PROBLEM

We've been going in circles because we don't understand how `txt CantChangeSize` actually works.

## Source Code Analysis

### PanelGraphiqueBase.drawString() - Lines 1046-1100

```java
float sx = (float) Zoom;
float sy = (float) Zoom;

if (txtCantChangeSize)  // If TRUE, text should stay constant size
{
    sx = 1.0f / Size;   // Typically Size=1.0f, so sx=1.0
    sy = 1.0f / Size;
}

// Apply axis inversions
if ((!invertXAxis) && (invertYAxis))  // CADWorkshopView case
{
    sy = -sy;   // Flip Y axis
}

// Build local transform
AffineTransform at2 = new AffineTransform();
at2.concatenate(at);  // Start with WORLD transform (zoom + scroll + inversions)

AffineTransform at3 = new AffineTransform();
at3.translate(x, y);              // Position in WORLD coords
at3.scale(1f / sx, 1f / sy);      // Scale factor
at3.rotate(Math.toRadians(ang));  // Rotation
at3.translate(-recenterx * sx, -recentery * sy);  // Alignment

at2.concatenate(at3);  // Combine: at2 = at * at3
```

### Transformation Pipeline

When `at2.transform(point)` is called, the order is:
1. `at3` transforms the point (LOCAL space)
2. `at` transforms the result (WORLD space)

So: `final_point = at * (at3 * point)`

### Where `at` Comes From (PanelGraphiqueBaseBase.paint() - Lines 481-487)

```java
at = new AffineTransform();
at.translate(getWidth() / 2.0, getHeight() / 2.0);  // Center viewport
at.scale(Zoom, Zoom);                                // Apply zoom
at.translate(ScrollX, ScrollY);                      // Apply scroll
at.scale(invertXAxis ? -1 : 1, invertYAxis ? -1 : 1); // Apply axis inversions
```

## COORDINATE SYSTEM QUESTION

**Does drawString() expect WORLD coordinates or SCREEN coordinates for (x, y)?**

Looking at line 1095:
```java
at3.translate(x, y);  // This is the FIRST operation in at3
```

And the full pipeline:
```
point → at3.translate(x, y) → ... → at (includes zoom) → screen
```

Since `at` contains the zoom/scroll/inversions, the (x, y) passed to `drawString()` are in **WORLD COORDINATES**.

## THE SIZE CANCELLATION MYSTERY

### Case 1: txtCantChangeSize=TRUE, Size=1.0f (EXPECTED: Constant screen size)

```
sx = 1.0f / 1.0f = 1.0f
sy = -1.0f (after Y-axis inversion)

at3.scale(1f / 1.0f, 1f / -1.0f) = scale(1.0, -1.0)

Combined with at which has scale(Zoom, Zoom) * scale(1, -1):
= scale(Zoom * 1.0, Zoom * -1.0)
= scale(Zoom, -Zoom)

Result: Text IS SCALED by zoom! (WRONG!)
```

### Case 2: txtCantChangeSize=FALSE (EXPECTED: Scales with zoom)

```
sx = Zoom
sy = -Zoom (after Y-axis inversion)

at3.scale(1f / Zoom, 1f / -Zoom) = scale(1/Zoom, -1/Zoom)

Combined with at which has scale(Zoom, Zoom) * scale(1, -1):
= scale(Zoom * 1/Zoom, Zoom * -1/Zoom)
= scale(1.0, -1.0)

Result: Text is NOT scaled! (WRONG - backwards!)
```

## THE BUG

**The current implementation has the logic BACKWARDS!**

When `txtCantChangeSize=TRUE`, text SHOULD stay constant but actually ZOOMS.
When `txtCantChangeSize=FALSE`, text SHOULD zoom but stays CONSTANT.

## WAIT... Let me check the test files to see if they actually work

Looking at test files, they all use `txtCantChangeSize=true` with `Size=1.0f` for normal text.

If the logic is backwards, those tests would be broken. But they're not!

**SO THERE MUST BE SOMETHING ELSE I'M MISSING!**

## HYPOTHESIS: The Size Parameter

What if `Size` is supposed to be set to `Zoom` when you want constant-size text?

```
When txtCantChangeSize=TRUE and Size=Zoom:

sx = 1.0f / Zoom
sy = -1.0f / Zoom (after Y inversion)

at3.scale(1f / (1.0f / Zoom), 1f / (-1.0f / Zoom))
   = scale(Zoom, -Zoom)

Combined with at: scale(Zoom, Zoom) * scale(1, -1) * scale(Zoom, -Zoom)
   = scale(Zoom * Zoom, -Zoom * Zoom)
   = scale(Zoom², -Zoom²)

Result: Text gets HUGE! (WRONG!)
```

No, that doesn't work either.

## NEXT STEP

I need to:
1. Create a MINIMAL test case
2. Actually RUN it and SEE what happens
3. Add debug output to see the actual transform values
4. Compare with working examples from the test files

The documentation says `txtCantChangeSize=true` keeps text at constant size, but the math doesn't support this!

**There must be something in the Graphics2D rendering or Font metrics that I'm not accounting for.**
