load('public/http/HttpType', function () {
  let httpType = {}
  httpType.ResultCode = {
    OK: 0,
    ParseError: 1,
    OnError: 2,
    FormatError: 3,
    TimeOut: 4,
    Abort: 5
  }
  httpType.ParseType = {
    Byte: 1,
    Text: 2,
    XXTea: 3
  }
  return httpType
})
