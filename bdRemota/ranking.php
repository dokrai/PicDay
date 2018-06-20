<?php

$conn = new mysqli("localhost","root","","picday_bd");

if($conn->connect_error){
	//echo "conexion failed";
}

$data = array();
$sql = "SELECT user_name,email,password,SUM(score) FROM usuarios NATURAL JOIN challenges NATURAL JOIN fotos WHERE user_id = owner_id AND challenge_id = pic_challenge GROUP BY user_name ORDER BY SUM(score) DESC;";
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