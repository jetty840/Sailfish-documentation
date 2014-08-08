LCDGEN=java -cp ../../tools/lcdgen Main
CONVERT=convert

SRC = $(wildcard *.jpg)
IMG = $(patsubst %.jpg,../pdf/%.jpg,$(SRC)) \
	$(patsubst %.jpg,../pdf/%.png,$(SRC)) \
	$(patsubst %.jpg,../pdf/%.eps,$(SRC)) \
	$(patsubst %.jpg,../pdf/%.pdf,$(SRC)) \
	$(patsubst %.jpg,../html/%.gif,$(SRC)) \
	$(patsubst %.jpg,../html/%.eps,$(SRC))

all: $(IMG)

clean:
	rm -f $(IMG)

../pdf/%.jpg : %.jpg
	$(CONVERT) -resize 75% $< $@

../pdf/%.png : %.jpg
	$(CONVERT) -resize 75% $< $@

../pdf/%.eps : %.jpg
	$(CONVERT) -resize 75% $< $@

../pdf/%.pdf : %.jpg
	$(CONVERT) -resize 75% $< $@

../html/%.gif : %.jpg
	$(CONVERT) -resize 50% $< $@

../html/%.eps : %.jpg
	$(CONVERT) -resize 50% $< $@

../html/keypad.gif : keypad.jpg
	$(CONVERT) -resize 25% $< $@

../html/keypad.eps : keypad.jpg
	$(CONVERT) -resize 25% $< $@

../html/nozzle-calibration-print.gif : nozzle-calibration-print.jpg
	$(CONVERT) $< $@

../html/nozzle-calibration-print.eps : nozzle-calibration-print.jpg
	$(CONVERT) $< $@

../html/jkn-advance-k.gif : jkn-advance-k.jpg
	$(CONVERT) $< $@

../html/jkn-advance-k.eps : jkn-advance-k.jpg
	$(CONVERT) $< $@

../html/jkn-advance-k2.gif : jkn-advance-k2.jpg
	$(CONVERT) $< $@

../html/jkn-advance-k2.eps : jkn-advance-k2.jpg
	$(CONVERT) $< $@