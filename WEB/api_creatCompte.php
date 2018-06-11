<?php 
	
	include 'connectionDB.php';

	if(isset($_POST['username']) && isset($_POST['password']) && isset($_POST['mail'])) 
	{
		$dataBase =  connectionDB();
		$username = $_POST['username'];//(int)$_POST["idSource"];
		$password =  hash("sha256",$_POST["password"]);//(int)$_POST['idConversation'];
		$mail = $_POST['mail'];

		$resultConv = $dataBase->prepare("SELECT login FROM User WHERE login = :username");
		$resultConv->bindParam(':username', $username, PDO::PARAM_STR);
		$resultConv->execute();

		//check if usernam existe in BDD
		if($resultConv->fetch(PDO::FETCH_ASSOC))
		{
			echo "false";
		}
		else
		{
			$resultConv = $dataBase->prepare("INSERT INTO User (login, password,cookie, pub_key) VALUES (:username , :password, 'NULL','NULL')");
			$resultConv->bindParam(':username', $username, PDO::PARAM_STR);
			$resultConv->bindParam(':password', $password, PDO::PARAM_STR);
			$resultConv->execute();
			echo "true";
		}
	}
	else
	{
		$dataBase = null;   // Disconnect
		echo "false";
	}
?>