<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UniFusion :: Announcements</title>
        <link rel="stylesheet" href="CSS/announcements.css">
        <link rel="stylesheet" href="CSS/all.css">
    </head>
    <body>
        <div id='bot' onclick='location.href="Chatbot"'>
            <img id='eugeo' src='https://www.flaticon.com/svg/static/icons/svg/3398/3398640.svg'>
            <a id='desc'>Need help?<br><span>Eugeo's here!</span></a>
        </div>
        
      <div id='container'>

        <div id='navbar'>
            <a href='Dashboard' id='back'>&lt; <span>Back</span></a>
            <a href='Home' id='scaffold'>Scaffold</a>
            <a href='Dashboard' class='link'>Dashboard</a>
            <a href='AccountDetails' class='link'>Account</a>
            <a href='Chatbot' class='link'>Chatbot</a>
        </div>

        <div id='top'>
          <div id='topOverlay'></div>
          <div id='info'>
            <img id='icon' src='${icon}'>
            <div id='text'>
              <a id='subheading'>${subheading}</a>
              <a id='heading'>Announcements</a>
            </div>
          </div>
        </div>

        <div id='search'>
          <select class='dropdown' id='searchAttribute'>
            <option value='posterName'>Poster name</option>
            <option value='itemName'>Item name</option>
          </select>
          <a class='dropdownLabel'>Click to view options</a>
          <input class='textbox' type='text' id='searchTextbox' placeholder='Search...'>
          <input id='search-button' class='button' type='submit' value='>' onclick='search()'>
            <a href='${postURL}' id='create-button' class='button'>Post an Announcement</a>
        </div>

        <div id='list'>
            
            ${announcementUI}

<!--           sample announcement 1 

          <div class='announcement'>
            <a class='time'>15m ago</a>
            <img class='icon' src='https://cdn.donmai.us/original/6f/90/__buratei_marii_joshiraku_drawn_by_taka_takahirokun__6f90a4d95e72eb6d5d0659af3a6efb9d.jpg'>
            <div class='text'>
              <a class='message'><span>Naganohara Mio</span> posted item</a>
              <a class='item'>Practical 5</a>
            </div>
          </div>

           sample announcement 2 

          <div class='announcement'>
            <a class='time'>3h ago</a>
            <img class='icon' src='https://cdn.donmai.us/original/6f/90/__buratei_marii_joshiraku_drawn_by_taka_takahirokun__6f90a4d95e72eb6d5d0659af3a6efb9d.jpg'>
            <div class='text'>
              <a class='message'><span>Minakami Mai</span> commented</a>
              <a class='item'>Assignment Materials</a>
            </div>
          </div>

           sample announcement 3 

          <div class='announcement'>
            <a class='time'>6h ago</a>
            <img class='icon' src='https://cdn.donmai.us/original/6f/90/__buratei_marii_joshiraku_drawn_by_taka_takahirokun__6f90a4d95e72eb6d5d0659af3a6efb9d.jpg'>
            <div class='text'>
              <a class='message'><span>Aioi Yuuko</span> posted item</a>
              <a class='item'>Assignment Materials</a>
            </div>
          </div>-->

        </div>

      </div>

    </body>

    <script>

    function search() {
            var query = document.getElementById("searchTextbox").value;
      var attribute = document.getElementById("searchAttribute").value;

      var announcements = document.getElementsByClassName('announcement');

      for (var i = 0; i < announcements.length; i++) {

        var itemName = announcements[i].children[2].children[0].children[0].textContent;
        var posterName = announcements[i].children[2].children[1].textContent;
        var data = "";

        if (attribute === "posterName") {
            data = posterName;
        } else if (attribute === "itemName") {
            data = itemName;
        } else {
            data = posterName;
        } 

        if (data.toLowerCase().includes(query.toLowerCase())) {
            announcements[i].style.display = "flex";
        } else {
            announcements[i].style.display = "none";
        }

      }
    }

    </script>
    
</html>
