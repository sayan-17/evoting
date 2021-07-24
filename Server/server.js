const http = require('http');
const fs = require('fs');
const { stringify } = require('querystring');

const server = http.createServer((req, res) => {
    console.log("Server called");
    console.log(req.method);
    console.log(stringify(req.chunk));
    if(req.method == "GET"){
        console.log("GET called");
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader('Access-Control-Allow-Methods', 'GET');
        res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,Content-type');
        res.setHeader('Access-Control-Allow-Credentials', true);
        res.end("error");
    }else if(req.method == "OPTIONS"){
        console.log("POST called");
        req.on(`data`, function(chunk){
            var data = chunk.toString();
            console.log(data);
        })
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader('Access-Control-Allow-Methods', 'POST');
        res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,Content-type');
        res.setHeader('Access-Control-Allow-Credentials', true);
        res.end("stored-data");
    }
} ); 

server.listen(8000,"127.0.0.1", () => {
 console.log("Server running on port 8000");
});

    // res.setHeader("Access-Control-Allow-Origin", "*");
    // res.setHeader('Access-Control-Allow-Methods', 'GET');
    // res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,Content-type');
    // res.setHeader('Access-Control-Allow-Credentials', true);
    // res.end("error");