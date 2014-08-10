all:
	cd tools/lcdgen; make
	cd images; make
	cd doc; make pdf; make html

clean:
	cd tools/lcdgen; make clean
	cd images; make clean
	cd doc; make clean
