#/bin/bash
#see https://en.wikibooks.org/wiki/LaTeX/Bibliography_Management#Why_won.27t_LaTeX_generate_any_output.3F
pdflatex rad.tex &&
bibtex rad.aux &&
pdflatex rad.tex &&
pdflatex rad.tex &&
rm *.blg *.log *.bbl *.aux