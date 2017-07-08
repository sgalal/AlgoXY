SRC = preface
OBJ = $(SRC)-en.pdf
OBJ_CN = $(SRC)-zh-cn.pdf
TEX = xelatex

.PHONY: all

all: tex
tex: $(OBJ) $(OBJ_CN)

cpp:
	$(MAKE) cpp -C src

c:
	$(MAKE) c -C src

haskell:
	$(MAKE) haskell -C src

tool:
	$(MAKE) tool -C src

image:
	$(MAKE) -C img

%.pdf: %.tex image
	$(TEX) $<
	$(TEX) $<

html: image
	htlatex.bat $(OBJECT) "html, imgdir:img/"
	mv *.png img/

clean:
	rm -f *.aux *.dvi *.log *.out *.pdf *~
