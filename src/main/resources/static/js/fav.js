$(function() {
	let favForm = $(".fav_form");
	$("button").each(function(i, btn){
		let btnId = $("#" + $(btn).attr("id"));
		if(btnId.val() == 1){
			btnId.css('background-color','yellow');
			console.log("ボタンカラー変更　　:" + btnId.val());
		}
	})
	favForm.on("submit", {passive: false}, function(e) {
	    e.preventDefault();  // デフォルトのイベント(ページの遷移やデータ送信など)を無効にする
		
		let btnId = $('#' + $(this).children('button').attr('id'));
		
		
		/* inputのvalueが0の時valueを1にしてボタンのカラー変更 */
		if(btnId.val() == 0){
			btnId.css('background-color','yellow');
			btnId.val(1);
		}else{
			btnId.css('background-color','');
			btnId.val(0);
		}
		console.log("ボタンのval:" + btnId.val());
		console.log("itemId:" + btnId.attr('id'));
		/* bookId,valueをjson形式にする */
		let json = {
			itemId: btnId.attr('id'),
			isFav: btnId.val()
		}
	
	    $.ajax({
	      url: $(this).attr("action"),  // リクエストを送信するURLを指定（action属性のurlを抽出）
	      type: "POST",  // HTTPメソッドを指定（デフォルトはGET）
	      data: {json: JSON.stringify(json)}, //送信データ
	      dataType: "json"
	    })
	    .done(function() {

	    })
	    .fail(function() {
	      alert("error!");  // 通信に失敗した場合の処理
	    })
	  });
});