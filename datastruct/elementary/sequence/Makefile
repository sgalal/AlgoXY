SRC = sequence
OBJ = $(SRC)-en.pdf
OBJ_CN = $(SRC)-zh-cn.pdf
TEX = xelatex

.PHONY: all

all: tex

cpp:
	$(MAKE) cpp -C src

haskell:
	$(MAKE) haskell -C src

image:
	$(MAKE) -C img

tex: $(OBJ) $(OBJ_CN)

%.pdf: %.tex image
	$(TEX) $<
	$(TEX) $<

html:
	htlatex $(OBJECT) "html, imgdir:img/"
	mv *.png img/

clean:
	rm -f *.4ct *.4tc *.aux *.css *.dvi *.html
	rm -f *.idv *.lg *.log *.out *.pdf *.tmp *.xref *.cache *.zip *~
