LCDGEN=java -cp ../../tools/lcdgen Main
CONVERT=convert -resize 50%

SRCTXT = $(wildcard ../*.txt)
IMGTXT = $(patsubst ../%.txt,%.gif,$(SRCTXT)) \
	$(patsubst ../%.txt,%.eps,$(SRCTXT))

SRC = $(SRCTXT)
IMG = $(IMGTXT)

all: $(IMG)

clean:
	rm -f $(IMG)

%.gif : ../%.txt
	$(LCDGEN) small $< $@

%.eps : %.gif
	$(CONVERT) $< $@

