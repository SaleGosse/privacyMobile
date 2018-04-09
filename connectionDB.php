<?php
	
	function connectionDB()
	{
		//connection with data base
		$servername = "localhost";
		try
		{
			//creat data base connection with pdo
			$dataB = new PDO("mysql:host=$servername;dbname=privacyDB", "root", "pass");

			// set the PDO error mode to exception
			$dataB->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
			//echo "Connected successfully";
		}
		catch(PDOException $e)
		{
			echo "Connection failed: " . $e->getMessage();
		}
		return $dataB;
	}
?>