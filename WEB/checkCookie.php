<?php
	
	function checkCookie($login, $cookie) {
		include 'connectionDB.php';
		
		$rq = "SELECT idUser FROM User WHERE User.login = :login AND User.cookie = :cookie";

		$dataB = connectionDB();

		$request = $dataB->prepare($rq);
		$request->bindParam(":login", $login, PDO::PARAM_STR);
		$request->bindParam(":cookie", $cookie, PDO::PARAM_STR);
		$request->execute();

		$result = $request->fetch();

		if($result)
			return true;

		return false;
	}

	if(isset($_POST["login"]) && isset($_POST["cookie"]))
	{
		$login = $_POST["login"];
		$cookie = $_POST["cookie"];

		if(checkCookie($login, $cookie))
			echo "true"; 
		else
			echo "false";
	}

	exit();
?>
