var fs = require("fs");
var LogType = require("./LogType");
var winston = require('winston');
var filePath = "./Command.log";

var myLog = new LogType();


var logger = new winston.Logger({
    transports: [
        new winston.transports.File({
            level: 'info',
            filename: './Command.log',
            handleExceptions: false,
            json: true,
            colorize: false
        })
    ],
    exitOnError: false
});

String.prototype.replaceAll = function (search, replacement) {
    var target = this;
    return target.split(search).join(replacement);
}


module.exports = function (app) {
    app.post('/exe', function (req, res) {
        var command = req.headers.command;
        logger.info(myLog.addLogExe(command));
        res.end();
    });

    app.post('/delete', function (req, res) {
        var command = req.headers.command;
        logger.info(myLog.addLogDelete(command));
        res.end();
    });

    app.post('/create', function (req, res) {
        var command = req.headers.command;
        logger.info(myLog.addLogCreate(command));
        res.end();
    });

    app.post('/rename', function (req, res) {
        var oldcommand = req.headers.oldcommand;
        var command = req.headers.command;
        logger.info(myLog.addLogRename(oldcommand, command));
        res.end();
    });

    app.post('/research', function (req, res) {
        var command = req.headers.command;
        logger.info(myLog.addLogResearch(command));
        res.end();
    });

    app.get('/CommandLog',function (req,res) {
        fs.readFile(filePath,function (err, data) {
            if(err){
                throw err;
            }
            var format = data.toString().replaceAll("}\r\n", "},").replaceAll('\"', '');
            res.json("[" + format.substring(0,format.length-1) + "]");
        })
    });
};