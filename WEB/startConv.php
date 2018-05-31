<?php

	include 'connectionDB.php';
	//include 'checkCookie.php';

	if(!isset($_POST["login"]) || !isset($_POST["cookie"]) || !isset($_POST["target"]))
	{
		echo "false\n";
		echo "Error: didn't receive the post data.";
		exit();
	}

	$login = $_POST["login"];
	$cookie = $_POST["cookie"];
	$targetLogin = $_POST["target"];

	$dataB =  connectionDB();


	$rq = "SELECT idUser,login FROM User WHERE User.login = :target";

	$request = $dataB->prepare($rq);
	$request->bindParam(":target", $targetLogin, PDO::PARAM_STR);
	$request->execute();

	$result = $request->fetchAll();

	if(!$result)
	{
		echo "false\n";
		echo "This user doesn't exist.";

		$dataB = null;
		
		exit();
	}

	if($result[0]["login"] === $login)
	{
		echo "false\n";
		echo "You can't chat with yourself.";

		$dataB = null;

		exit();
	}

	echo "true\n"
?>