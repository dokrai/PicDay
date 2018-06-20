<?php

$conn = new mysqli("localhost","root","","picday_bd");

if($conn->connect_error){
	echo "conexion failed";
}

$name = $_POST["name"];
$password = $_POST["password"];
$response["success"] = false;

$sql = "SELECT * FROM usuarios WHERE user_name = '".$name. "' AND password = '".$password."';";
$result = $conn->query($sql);

if(mysqli_num_rows($result) != 0){
	$response["success"]=true;
}

echo json_encode($response);

$conn->close();
?>