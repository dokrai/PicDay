<?php

$conn = new mysqli("localhost","root","","picday_bd");

if($conn->connect_error){
	//echo "conexion failed";
}

$data = array();
$sql = "SELECT * FROM challenges;";
$result = $conn->query($sql);

$i = 0;

if(mysqli_num_rows($result) != 0){
	while($row = $result->fetch_assoc()){
		$data[$i] = $row;
		$i++;
	}	 
}

echo json_encode(array("data"=>$data));


$conn->close();
?>