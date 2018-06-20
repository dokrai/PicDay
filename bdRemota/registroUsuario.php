<?php

$conn = new mysqli("localhost","root","","picday_bd");

if($conn->connect_error){
	//echo "conexion failed";
}

$name = $_POST["name"];
$email = $_POST["email"];
$password = $_POST["password"];
$time = date("Y-m-d");
$response["success"] = false;


$sql = "SELECT * FROM usuarios WHERE user_name = '".$name."';";
$result = $conn->query($sql);

if(mysqli_num_rows($result) == 0){
	$sql2 = "INSERT INTO usuarios (user_name,email,password,date) values ('" .$name. "','" .$email. "','" .$password."','".$time."');";
	$result2 = $conn->query($sql2);
	if($result2 ==true){
		$response["success"] = true;
	}
}

echo json_encode($response);


$conn->close();
?>