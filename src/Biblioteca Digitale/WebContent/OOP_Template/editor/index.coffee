`
// noprotect
`

# MTSS EDITOR

CodeMirror.defineSimpleMode('mtss', {
  start: [
    {regex: new RegExp('\\|\\|'), token: 'sentence'},
    {regex: new RegExp('(\\[)([^\\]]*)(\\])(\\()([^\\)]*)(\\))'), token: ['choice_square','choice_abbr','choice_square','choice_round','choice_expan','choice_round']},
    {regex: new RegExp('{{'), token: 'w', next: 'w'}
  ],
  w: [
    {regex: new RegExp('}}'), token: 'w', next: 'start'},
    {regex: new RegExp('.'), token: 'w_content'}
  ]
})


editor = CodeMirror.fromTextArea document.getElementById('editor'), {
  mode: 'mtss',
  lineNumbers: true,
  lineWrapping: true
}


# TEI translation

editor.on 'change', () -> update_code()
  
update_code = () ->
  mtss = editor.getValue()
  
        # opening sentence in the first folio
  tei = '<s class="sentence">\n    <lb/>' + mtss
    # line break
    .replace(new RegExp('\n','g'), '\n    <lb/>')
    # sentence end mark
    .replace(new RegExp('\\|\\|','g'), '\n</s>\n<s class="sentence">')
    # <w> tag
    .replace(new RegExp('{{','g'), '<w>')
    .replace(new RegExp('}}','g'), '</w>')
    # <choice> tag
    .replace(new RegExp('\\[([^\\]]*)\\]\\(([^\\)]*)\\)','g'), '<choice><abbr>$1</abbr><expan>$2</expan></choice>')
    
  # closing sentence in the last folio
  tei += '\n</s>'
    
  # lb numbering
  for i in [1..99] # WARNING this is a dirty hack
    tei = tei
      .replace('<lb/>', "<lb n=\"#{d3.format('02d')(i)}\"/>")
  
  # s numbering
  for i in [1..99] # WARNING this is a dirty hack
    tei = tei
      .replace('<s class="sentence">', "<s class=\"sentence\" n=\"s_#{d3.format('02d')(i)}\">")
      
  code_el = d3.select('#code > code')
  
  code_el.text(tei)
    
  # update syntax highlighting
  hljs.highlightBlock(code_el.node())
  
update_code()


# Sentence highlighting

current_sentence = null

editor.on 'cursorActivity', () ->
  cursor = editor.getCursor()
  
  search_cursor = editor.getSearchCursor('||', cursor)
  
  search_cursor.findPrevious()
  from = search_cursor.pos.to
  
  search_cursor.findNext()
  to = search_cursor.pos.from
  
  if current_sentence?
      current_sentence.clear()
  
  current_sentence = editor.markText(from, to, {className: 'sentence_highlight'})
	  