all:
	cd tools/lcdgen; make
	cd images; make

clean:
	cd tools/lcdgen; make clean
	cd images; make clean
