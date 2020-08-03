#! /bash/bin
function build() {
    npm run build
}

function run() {
    if [ ! -x ./node_modules ]; then npm install; fi;
    npm run serve
}
function copy() {
    rm -r ../clairvoyance-web/src/main/resources/static/
    cp -r ./dist/ ../clairvoyance-web/src/main/resources/static
}

function buildAndCopy() {
    build
    copy
}

a=$1




if [ $a == "build" ]; then
    build
elif [ $a == "run" ]; then
    run
elif [ $a == "copy" ]; then
    copy
else buildAndCopy

fi
