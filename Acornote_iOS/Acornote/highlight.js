// TODO
var replaceAll = function (str, find, replace) {
    return str.replace(new RegExp(find, 'i'), replace);
}

var highlight = function(texts, color) {
    if (texts.length == 0){
        return "length == 0"
    }
    var text = document.body.innerHTML
    if (text.length == 0) {
        return "empty document"
    }
    
    texts.forEach(function(item){
                   text = replaceAll(text, item, "<strong style='color:" + color + "'>"+item+"</strong>")
                  })
    document.body.innerHTML = text
    
    return "highlight success"
}
