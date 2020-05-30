<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UniFusion :: Account Details</title>
        <link rel="stylesheet" href="accountDetails.css">
    </head>
    <body>
  <div id='container'>
  
    <a href='#' id='back'>&lt; Back</a> <!-- future note: to redirect to account.jsp (main profile page), or something similar -->

    <div id='top'>
      <a id='heading'>Account details</a>
    </div>

    <form id='bottom'>

      <div id='left'>
      <a class='label' id='name'>Name</a>
        <input class='textbox' type='text' name='name' placeholder='Name'>
        <a class='label' id='age'>Age</a>
        <input class='textbox' type='text' name='age' placeholder='Age'>
        <a class='label' id='address'>Address</a>
        <input class='textbox' type='text' name='address' placeholder='Address'>
      </div>

      <div id='right'>
        <a class='label' id='email'>Email</a>
        <input class='textbox' type='text' name='email' placeholder='Email'>
        <a class='label' id='email'>Password</a>
        <input class='textbox' type='password' name='password' placeholder='Password'>
        <a class='label' id='email'>Confirm password</a>
        <input class='textbox' type='password' name='confirmPassword' placeholder='Confirm password'>
      </div>

    </form>

  </div>
</body>

</html>
