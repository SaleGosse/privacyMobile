<?php 
	
	include 'connectionDB.php';

	if(isset($_POST['cookie']))
	{	
		$db =  connectionDB();
		
		$cookie = (string)$_POST['cookie'];
			
		//The different request we'll use
		$rq_check_cookie = "SELECT idUser FROM User WHERE cookie = :cookie AND idUser = :userID";

		//Getting the POST parameters as variables
		$conversationID = (int)$_POST['conversationID'];
		$content = $_POST["content"];
		$userID = (int)$_POST["userID"];
		
		//Checking the cookie, we're never too sure.
		$request = $db->prepare($rq_check_cookie);
		$request->bindParam(':cookie', $cookie, PDO::PARAM_STR);
		$request->bindParam(':userID', $userID, PDO::PARAM_INT);
		$request->execute();
		$result = $request->fetch();

		if(!$result)
		{
			echo "false\n";
			echo "error: Invalid cookie.\n";
		
			$db = null;

			//Exiting
			exit();
		}
		
		if(isset($_POST['content']) && isset($_POST['userID']) && isset($_POST['conversationID']))
		{
			

			$currentDate = date('Y-m-d H:i:s', time());

			// load message on server 
			$rq_insert_message = "INSERT INTO Message (idConversation,  idUser, content,date) VALUES (:conversationID, :userID, :content, :currentDate)";
			$request = $db->prepare($rq_insert_message);
			$request->bindParam(':conversationID', $conversationID, PDO::PARAM_INT);
			$request->bindParam(':userID', $userID, PDO::PARAM_INT);
			$request->bindParam(':content', $content, PDO::PARAM_STR);
			$request->bindParam(':currentDate', $currentDate, PDO::PARAM_STR);
			$request->execute();

			// take id Message
			$rq_get_message_ID = "SELECT idMessage FROM Message WHERE idConversation = :conversationID AND idUser = :userID AND date = :currentDate";
			$request = $db->prepare($rq_get_message_ID);
			$request->bindParam(':conversationID', $conversationID, PDO::PARAM_INT);
			$request->bindParam(':userID', $userID, PDO::PARAM_INT);
			$request->bindParam(':currentDate', $currentDate, PDO::PARAM_STR);
			$request->execute();

			$messageID = $request->fetch();
			$messageID = $messageID['idMessage'];

			$rq_get_conversation_users = "SELECT idUser FROM linkConversation l WHERE l.idConversation = :conversationID AND l.idUser != :userID";
			$request = $db->prepare($rq_get_conversation_users);
			$request->bindParam(':conversationID', $conversationID, PDO::PARAM_INT);
			$request->bindParam(':userID', $userID, PDO::PARAM_INT);
			$request->execute();

			$users = $request->fetchAll(PDO::FETCH_ASSOC);
			
			$rq_add_notif = "INSERT INTO MessageStatus (idUser,idMessage,unread,notified) VALUES (:userID,:messageID,1,0)";
			$request = $db->prepare($rq_add_notif);

			for ($i=0; $i < count($users); $i++) {
				$request->bindParam(':userID', $users[$i]['idUser'], PDO::PARAM_INT);
				$request->bindParam(':messageID', $messageID, PDO::PARAM_INT);

				$request->execute();
			}

			echo "true";

			$db = null;   // Disconnect
		}
		else 
			echo "error: Invalid or missing POST parameters.\n";
	}
	else
		echo "error: Missing cookie.\n";
?>