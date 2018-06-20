<?php

$conn = new mysqli("localhost","root","","picday_bd");

if($conn->connect_error){
	//echo "conexion failed";
}

$owner = $_POST["owner"];
$data = array();

$sql = "SELECT foto_id,foto_name,user_name,img,challenge_name,score,times_scored FROM fotos NATURAL JOIN challenges NATURAL JOIN usuarios WHERE user_id = owner_id AND challenge_id = pic_challenge AND user_name = '".$owner."';";

$result = $conn->query($sql);

$i = 0;

if(mysqli_num_rows($result) != 0){
	while($row = $result->fetch_assoc()){
		$data[$i] = $row;
		$i++;
	}
	echo json_encode(array("data"=>$data));	 

}

$conn->close();
?>