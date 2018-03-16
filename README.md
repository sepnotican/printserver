# project: printserver
author: Muradov MA, 2018

Simple printserver for pdf and ZPL printing via http requests. Tested on linux ubuntu with configured CUPS.

## Usage of printserver:

### method: POST /print
#### description:
prints a binary data from octet stream to printer pointed in printerAddress header

#### headers:
printType : ZPLSOCKET, PDFLOCAL.
printerAddress : A printer name in server system (can be got from OPTIONS /print call) for PDFLOCAL printer mode
OR IP:PORT address for ZPLSOCKET mode (ex. "192.168.1.1:9100")
#### body: 
octet-stream with data to print.

charset must be set in Content-Type header (ex. "application/octet-stream; charset=windows-1251")

### method OPTIONS /print
returns a json list of printers names int the server system

### method GET /print
returns a list of printers names with a supported doc flavours
