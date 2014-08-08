This repository contains sources for the Sailfish Reference Manual
written and Copyright (c) 2014, Laurel Newman.  See the file
`LICENSE.txt` for licensing terms.

To produce the documentation contained here, the following tools
are needed

1. LaTeX distribution including the TeX engine, pdflatex, and htlatex
   (tex4ht).

2. Ghostscript (used by pdflatex and htlatex).

3. Image Magick (used by pdflatex, htlatex, and the make procedures).

4. A JDK and JRE for comiling and running Java (used to make simulated
   LCD screen snapshots).

5. Any tools, libraries, etc. needed by the above.

To build the documenation, just issue the command

	make

from the top-level directory.  The final `sailfish-reference.pdf`
and `sailfish-reference.html` file (plus linked pages and images)
will be output to the directory `doc/output/`.

The command

	make clean

may be used to remove all intermediate and final files.
