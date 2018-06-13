<?php

	include 'checkCookie.php';

	if(isset($_POST['userID']) && isset($_POST['cookie']))
	{
		include 'connectionDB.php';

		//Getting the POST params
		$userID = (int)$_POST["userID"];	
		$cookie = (string)$_POST['cookie'];

		$db =  connectionDB();
		
		if(!checkCookie($db, $userID, $cookie))
		{
			//Printing the error
			echo "false\n" . "error: Invalid cookie.\n";

			//Closing the db and exiting
			$db = null;
			exit();
		}

		if(isset($_POST["conversationID"]) && isset($_POST["invitationID"]) && isset($_POST["action"]))
		{
			$action = (string)$_POST["action"];
			$conversationID = (int)$_POST["conversationID"];
			$invitationID = (int)$_POST["invitationID"];
			
			//Getting the id of the invitation submitter
			$rq_get_fromID = "SELECT idUser FROM Invitation WHERE idInvitation = :invitationID";
			$request = $db->prepare($rq_get_fromID);

			$request->bindParam("invitationID", $invitationID, PDO::PARAM_INT);
			$request->execute();

			$fromID = $request->fetch();
			$fromID = $fromID['idUser'];

			if($action == "accept")
			{
				$rq_update_invit = "UPDATE Invitation SET isOK = 1 WHERE idInvitation = :invitationID";
				$request = $db->prepare($rq_update_invit);

				$request->bindParam(":invitationID", $invitationID, PDO::PARAM_INT);
				
				$request->execute();
			}
			else if ($action == "decline")
			{
				//Deleting the links in linkConversation
				$rq_del_link = "DELETE FROM linkConversation WHERE idUser = :userID AND idConversation = :conversationID";
				$request = $db->prepare($rq_del_link);

				$request->bindParam(":conversationID", $conversationID, PDO::PARAM_INT);

				//For us
				$request->bindParam(":userID", $userID, PDO::PARAM_INT);
				$request->execute();

				//For the issuer of the invit
				$request->bindParam(":userID", $fromID, PDO::PARAM_INT);
				$request->execute();

				//Deleting the conversation
				$rq_del_conv = "DELETE FROM Conversation WHERE idConversation = :conversationID";
				$request = $db->prepare($rq_del_conv);
				$request->bindParam(":conversationID", $conversationID, PDO::PARAM_INT);
				$request->execute();

				//Deleting the invitation
				$rq_del_invit = "DELETE FROM Invitation WHERE idInvitation = :invitationID";
				$request = $db->prepare($rq_del_invit);
				$request->bindParam(":invitationID", $invitationID, PDO::PARAM_INT);
				$request->execute();

				echo "true\n";
			}
			else
				echo "false\n" . "error: Unhandled action.\n";


		}
		else
			echo "error: Missing POST parameters.\n";

		$db = null;
	}
	else
		echo "false\n" . "error: Invalid or missing cookie.\n";


?>