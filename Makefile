BOOK = main-en
BOOK_CN = main-zh-cn
XELATEX = $(shell which xelatex > /dev/null)

ifdef XELATEX
LATEX = xelatex
DVIPDFM = echo
else
LATEX = latex
DVIPDFM = dvipdfmx
endif

#SRC = common main
CHAPTERS = others/preface/preface \
datastruct/tree/binary-search-tree/bstree \
sorting/insertion-sort/isort \
datastruct/tree/red-black-tree/rbtree datastruct/tree/AVL-tree/avltree \
datastruct/tree/trie/trie \
datastruct/tree/suffix-tree/stree datastruct/tree/B-tree/btree \
datastruct/heap/binary-heap/bheap sorting/select-sort/ssort \
datastruct/heap/other-heaps/kheap \
datastruct/elementary/queue/queue \
datastruct/elementary/sequence/sequence \
sorting/dc-sort/dcsort \
search/search \
others/appendix/list/list \
others/appendix/haskell/haskell

CHAPTER_SRCS = $(foreach file, $(CHAPTERS), $(file)-en.tex)
CHAPTER_OBJS = $(foreach file, $(CHAPTERS), $(file)-en.pdf)

CHAPTER_CN_SRCS = $(foreach file, $(CHAPTERS), $(file)-zh-cn.tex)
CHAPTER_CN_OBJS = $(foreach file, $(CHAPTERS), $(file)-zh-cn.pdf)

all: $(BOOK) #$(BOOK_CN)

%.pdf : %.tex
	echo $(@D)
	$(MAKE) -C $(@D) tex

image:
	$(MAKE) -C img

index:
	makeindex $(BOOK)
	makeindex $(BOOK_CN)

$(BOOK): image $(CHAPTER_OBJS)
	$(LATEX) $(BOOK).tex
	makeindex $(BOOK).idx
	$(LATEX) $(BOOK).tex
	$(DVIPDFM) $(BOOK)

$(BOOK_CN): image $(CHAPTER_CN_OBJS)
	$(LATEX) $(BOOK_CN).tex
	makeindex $(BOOK_CN).idx
	$(LATEX) $(BOOK_CN).tex
	$(DVIPDFM) $(BOOK_CN)

clean:
	rm -f *.aux *.toc *.lon *.lor *.lof *.ilg *.idx *.ind *.out *.log *.exa

distclean: clean
	rm -f *.pdf *.dvi *~
