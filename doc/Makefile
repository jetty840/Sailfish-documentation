SRCTEX = $(wildcard *.tex)
OUTDIR = output

all:
	echo "Nothing to see here"

pdf: $(SRCTEX)
	mkdir -p $(OUTDIR)
	# Perform pdflatex builds with hyperref package for PDF linking
	pdflatex --file-line-error --synctex=1 sailfish
	makeindex -s sailfish.ist -t sailfish.alg -o sailfish.acr sailfish.glo
	makeindex -s sailfish.ist -t sailfish.glg -o sailfish.gls sailfish.glo
	makeindex sailfish
	pdflatex --file-line-error --synctex=1 sailfish
	makeindex -s sailfish.ist -t sailfish.alg -o sailfish.acr sailfish.glo
	makeindex -s sailfish.ist -t sailfish.glg -o sailfish.gls sailfish.glo
	makeindex sailfish
	pdflatex --file-line-error --synctex=1 sailfish
	mv sailfish.pdf $(OUTDIR)/sailfish.pdf
	rm -f *.aux sailfish.acr sailfish.alg sailfish.glg sailfish.glo \
		sailfish.gls sailfish.glsdefs sailfish.idx sailfish.ilg \
		sailfish.ind sailfish.ist sailfish.log sailfish.out \
		sailfish.synctex.gz sailfish.toc
	rm -f idxmake.dvi idxmake.log sailfish.4ct sailfish.4dx sailfish.4ix \
		sailfish.4tc sailfish.dvi sailfish.idv sailfish.lg \
		sailfish.tmp sailfish.xref texput.log

html: $(SRCTEX)
	mkdir -p $(OUTDIR)
	# Perform pdflatex builds without hyperref package for htlatex
	pdflatex --file-line-error --synctex=1 '\def\htmlflag{}\input{sailfish}'
	makeindex -s sailfish.ist -t sailfish.alg -o sailfish.acr sailfish.glo
	makeindex -s sailfish.ist -t sailfish.glg -o sailfish.gls sailfish.glo
	makeindex sailfish
	pdflatex --file-line-error --synctex=1 '\def\htmlflag{}\input{sailfish}'
	# note we could do htlatex <file> <switches> "" -d$(OUTPUT)
	# but that still manages to leave files all over
	htlatex sailfish "../configs/myfile.cfg,html,3,next,fn-in"
	tex '\def\filename{{sailfish}{idx}{4dx}{ind}} \input idxmake.4ht'
	makeindex -o sailfish.ind sailfish.4dx
	htlatex sailfish "../configs/myfile.cfg,html,3,next,fn-in"
	mv *.html $(OUTDIR)/
	mv *.css $(OUTDIR)/
	mv *.png $(OUTDIR)/
	../tools/bin/image-ref.sh $(OUTDIR)
	rm -f *.aux sailfish.acr sailfish.alg sailfish.glg sailfish.glo \
		sailfish.gls sailfish.glsdefs sailfish.idx sailfish.ilg \
		sailfish.ind sailfish.ist sailfish.log sailfish.out \
		sailfish.synctex.gz sailfish.toc
	rm -f idxmake.dvi idxmake.log sailfish.4ct sailfish.4dx sailfish.4ix \
		sailfish.4tc sailfish.dvi sailfish.idv sailfish.lg \
		sailfish.tmp sailfish.xref sailfish.pdf textput.log

clean:
	if [ -d $(OUTDIR) ]; then rm -f $(OUTDIR)/*; fi
	rm -f *.aux sailfish.acr sailfish.alg sailfish.glg sailfish.glo \
		sailfish.gls sailfish.glsdefs sailfish.idx sailfish.ilg \
		sailfish.ind sailfish.ist sailfish.log sailfish.out \
		sailfish.synctex.gz sailfish.toc
	rm -f idxmake.dvi idxmake.log sailfish.4ct sailfish.4dx sailfish.4ix \
		sailfish.4tc sailfish.dvi sailfish.idv sailfish.lg \
		sailfish.tmp sailfish.xref sailfish.lot *.html *.png *.pdf \
		*.css textput.log
