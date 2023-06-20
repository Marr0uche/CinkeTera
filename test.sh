javac -encoding utf8 -d "./bin" "@compile.list"
cd bin/
java controleur.Controleur -cp "./bin"
cd ..