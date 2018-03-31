<?php 
	$id=$_GET['id'];
	$tv="select * from menu_ngang where id='$id'";
	$tv_1=mysql_query($tv);
	$tv_2=mysql_fetch_array($tv_1);
	echo "<h1>";
		echo $tv_2['ten'];
	echo "</h1>";
	echo $tv_2['noi_dung'];
?>