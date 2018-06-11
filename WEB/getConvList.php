<?php

include 'connectionDB.php';


if(isset($_POST['cookie']))
{
	//Check the cookie
	$cookie = $_POST['cookie'];

	if(isset($_POST['userID']))
	{
		//Check the user id and get his convs
		$userID = (int)$_POST['userID'];

		$rq = "SELECT L.idConversation, C.convName AS name, 'content', C.lastDate AS date, IF(EXISTS(SELECT ms.idMessageStatus FROM MessageStatus ms JOIN Message m ON m.idMessage = ms.idMessage WHERE ms.idUser = L.idUser AND m.idConversation = L.idConversation AND ms.unread = 1), 'true', 'false') unread  FROM linkConversation L JOIN Conversation C ON L.idConversation = C.idConversation WHERE L.idUSer = :idUser";

		$dataB = connectionDB();

		$request = $dataB->prepare($rq);
		$request->bindParam(":idUser", $userID, PDO::PARAM_INT);

		$request->execute();

		$result = $request->fetchAll();

		echo json_encode($result) . "\n";

		exit();

	}
	else
		echo "false" . "\n" . "error: Invalid or missing user ID.";
}
else
	echo "false" . "\n" . "error: Invalid or missing cookie.";



?>
