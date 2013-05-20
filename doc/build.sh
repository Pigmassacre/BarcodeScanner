#/bin/bash
#see https://en.wikibooks.org/wiki/LaTeX/Bibliography_Management#Why_won.27t_LaTeX_generate_any_output.3F
pdflatex sdd.tex &&
bibtex sdd.aux &&
pdflatex sdd.tex &&
pdflatex sdd.tex &&
rm *.blg *.log *.bbl *.aux