(function() {
  
// noprotect
;
  var current_sentence, editor, update_code;

  CodeMirror.defineSimpleMode('mtss', {
    start: [
      {
        regex: new RegExp('\\|\\|'),
        token: 'sentence'
      }, {
        regex: new RegExp('(\\[)([^\\]]*)(\\])(\\()([^\\)]*)(\\))'),
        token: ['choice_square', 'choice_abbr', 'choice_square', 'choice_round', 'choice_expan', 'choice_round']
      }, {
        regex: new RegExp('{{'),
        token: 'w',
        next: 'w'
      }
    ],
    w: [
      {
        regex: new RegExp('}}'),
        token: 'w',
        next: 'start'
      }, {
        regex: new RegExp('.'),
        token: 'w_content'
      }
    ]
  });

  editor = CodeMirror.fromTextArea(document.getElementById('editor'), {
    mode: 'mtss',
    lineNumbers: true,
    lineWrapping: true
  });

  editor.on('change', function() {
    return update_code();
  });

  update_code = function() {
    var code_el, i, mtss, tei, _i, _j;

    mtss = editor.getValue();
    tei = '<s class="sentence">\n    <lb/>' + mtss.replace(new RegExp('\n', 'g'), '\n    <lb/>').replace(new RegExp('\\|\\|', 'g'), '\n</s>\n<s class="sentence">').replace(new RegExp('{{', 'g'), '<w>').replace(new RegExp('}}', 'g'), '</w>').replace(new RegExp('\\[([^\\]]*)\\]\\(([^\\)]*)\\)', 'g'), '<choice><abbr>$1</abbr><expan>$2</expan></choice>');
    tei += '\n</s>';
    for (i = _i = 1; _i <= 99; i = ++_i) {
      tei = tei.replace('<lb/>', "<lb n=\"" + (d3.format('02d')(i)) + "\"/>");
    }
    for (i = _j = 1; _j <= 99; i = ++_j) {
      tei = tei.replace('<s class="sentence">', "<s class=\"sentence\" n=\"s_" + (d3.format('02d')(i)) + "\">");
    }
    code_el = d3.select('#code > code');
    code_el.text(tei);
    return hljs.highlightBlock(code_el.node());
  };

  update_code();

  current_sentence = null;

  editor.on('cursorActivity', function() {
    var cursor, from, search_cursor, to;

    cursor = editor.getCursor();
    search_cursor = editor.getSearchCursor('||', cursor);
    search_cursor.findPrevious();
    from = search_cursor.pos.to;
    search_cursor.findNext();
    to = search_cursor.pos.from;
    if (current_sentence != null) {
      current_sentence.clear();
    }
    return current_sentence = editor.markText(from, to, {
      className: 'sentence_highlight'
    });
  });

}).call(this);