<?php

include 'connectionDB.php';

if(isset($_POST['userID']))
{
	$userID = $_POST['userID'];

	$db = connectionDB();

	$rq_get_notif = "SELECT ms.idMessageStatus AS ID FROM MessageStatus ms LEFT JOIN Message m ON m.idMessage = ms.idMessage WHERE ms.notified = 1 AND ms.idUser = :userID";
	$rq_update_notif = "UPDATE MessageStatus SET notified=1 WHERE idMessageStatus = :id";

	$request = $db->prepare($rq);
	$request->bindParam(":userID", $userID, PDO::PARAM_INT);
	$request->execute();

	$unread = $request->fetchAll();
	$request = $db->prepare($rq_update_notif);


	for ($i=0; $i < count($result); $i++) { 
		$request->bindParam(":id", $unread[0]['ID'], PDO::PARAM_INT);
		$request->execute();
	}

	echo "true\n" . json_encode($result) . "\n";

}
else 
	echo "error: Missing POST parameters.";


?>