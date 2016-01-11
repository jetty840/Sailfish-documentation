LCDGEN=java -cp ../../tools/lcdgen Main
CONVERT=convert -resize 75%

SRCTXT = $(wildcard ../*.txt)
IMGTXT = $(patsubst ../%.txt,%.gif,$(SRCTXT)) \
	$(patsubst ../%.txt,%.png,$(SRCTXT)) \
	$(patsubst ../%.txt,%.eps,$(SRCTXT)) \
	$(patsubst ../%.txt,%.pdf,$(SRCTXT))

SRC = $(SRCTXT)
IMG = $(IMGTXT)

all: $(IMG)

clean:
	rm -f $(IMG)

%.gif : ../%.txt
	$(LCDGEN) small $< $@

%.png : %.gif
	$(CONVERT) $< $@

%.pdf : %.gif
	$(CONVERT) $< $@

%.eps : %.gif
	$(CONVERT) $< $@
