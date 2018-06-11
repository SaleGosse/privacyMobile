	
<!DOCTYPE html>

<html lang="fr-FR">

	<head>
		<title> OuaisOuais  </title>
		<!-- <link rel="stylesheet" type="text/css" href="/style/style.css" /> -->
		<!-- <script type="text/javascript" src="/javascript/javascript.js"></script> -->
		<!-- <link rel="stylesheet" type="text/css" href="/bootstrap/css/bootstrap.min.css"/> -->
		<!-- <script src="/bootstrap/js/bootstrap.min.js"></script> -->
		<script type="text/javascript" src="/jquery.js"></script>
	</head>

	<body class="index" >
		<div class="parentPepito">
			<div class="pepito">
		 		<div class="pepitoInput">
					<label > Message  </label>
		 			
			 		<input type="text" class="Message" name="Message" placeholder="Message" required /> 

		    	 		<label> UserSource	</label>
					<input type="text" class="pseudo" name="UserSource" placeholder="idUser" required />

					<label> id Conversation </label>
					<input type="text" class="conversation" name="conversation" placeholder="idconversation" required />	
		 		</div>
		 		
				<div class="relative3">
	    			<input class="inputSend" type="submit" value="Send" onclick="sendMessage()" />
				</div>
			</div>
		</div>
		
		<div id="retour"> </div> 

		<script type="text/javascript"> 
			function sendMessage()
 			{
				var pseudo = document.getElementsByClassName("pseudo");
     			var message = document.getElementsByClassName("Message");
     			var conversation = document.getElementsByClassName("conversation");

     			$.ajax({
		            url : '/api_add_message.php',
		            type : 'POST',
		            data : 'cookie=a&' + 'userID=' + pseudo[0].value  + '&content=' + message[0].value + '&conversationID=' + conversation[0].value,
            		success : function(code_html){
            			$('#retour').html(code_html);
            		}
        		});   
 			}
		</script>
	</body>	
</html>
