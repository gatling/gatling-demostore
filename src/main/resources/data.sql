INSERT INTO admin (id, username, password)
VALUES (1,'admin','admin');

INSERT INTO categories (id, name, slug, sorting)
VALUES (5,'For Him','for-him',100),
       (6,'For Her','for-her',100),
       (7,'Unisex','unisex',100);

INSERT INTO pages (id, title, slug, content, sorting)
VALUES (1,'Home','home','<h2>Welcome to the Gatling DemoStore!</h2><p>&nbsp;</p><p>This is a fictional / dummy eCommerce store that sells eyeglass cases.</p><p>Since this is a sandbox solution, no actual sales transactions are completed on this site.</p><p>&nbsp;</p><p>You can login with the user admin (password <code>admin</code>) or users john, user1, user2 and user3 (password <code>pass</code>)</p><p>&nbsp;</p><h3><strong>&lt; Select a category from the left-hand menu</strong></h3><p>&nbsp;</p><p>The purpose of this store is to teach the creation of test scripts with the <a href="https://gatling.io">Gatling</a> load testing tool.</p><p>To find out more, check out the <a href="https://gatling.io/academy/">Gatling Academy</a></p>',0),
       (10,'About us','about-us','<h2>About Us</h2><p>&nbsp;</p><p>This is a fictional / dummy eCommerce store that sells eyeglass cases.</p><p>The purpose of this site is to teach the creation of test scripts with the <a href="https://gatling.io">Gatling</a> load testing tool.</p>',100),
       (11,'Contact','contact','<h2>Contact Us</h2><p>&nbsp;</p><p>To learn how this site can help teach you the creation of load testing scripts, check out the <a href="https://gatling.io/academy/">Gatling Academy</a>.</p><p>If you need to contact the Gatling team directly, check the <a href="https://gatling.io/company/contact/">Contact Us</a> page on the Gatling website.</p>',100),
       (12,'API','rest-api','<h2>REST API</h2><p>&nbsp;</p><p>The Demo Store also includes a REST API. Use these resources to explore the API:</p><ul><li><a href="/swagger-ui">Swagger UI</a></li><li><a href="/downloads/gatling-demostore-api-postman.zip">Postman collection</a></li></ul>',100);

INSERT INTO products (id, name, slug, description, image, price, category_id, created_at, updated_at)
VALUES (17,'Casual Black-Blue','casual-black-blue','<p>Some casual black &amp; blue glasses</p>','casual-blackblue-open.jpg',24.99,5,'2020-11-10 10:05:14','2020-11-10 10:05:14'),
       (19,'Black and Red Glasses','black-and-red-glasses','<p>A Black &amp; Red glasses case</p>','casual-blackred-open.jpg',18.99,5,'2020-11-15 16:52:20','2020-11-15 16:52:21'),
       (20,'Bright Yellow Glasses','bright-yellow-glasses','<p>A glasses case that is bright yellow</p>','casual-blue-open.jpg',17.99,5,'2020-11-15 16:54:22','2020-11-15 16:54:22'),
       (21,'Casual Brown Glasses','casual-brown-glasses','<p>A vintage glasses case in casual brown</p>','casual-brown-open.jpg',13.99,5,'2020-11-15 16:55:03','2020-11-15 16:55:03'),
       (22,'Deepest Blue','deepest-blue','<p>Glasses case with a deep blue design</p>','casual-darkblue-open.jpg',29.99,5,'2020-11-15 16:56:01','2020-11-15 16:56:01'),
       (23,'Light Blue Glasses','light-blue-glasses','<p>A glasses case in light blue</p>','casual-lightblue-open.jpg',17.99,5,'2020-11-15 16:56:48','2020-11-15 16:56:48'),
       (24,'Sky Blue Case','sky-blue-case','<p>A perfect sky blue glasses case</p>','casual-skyblue-open.jpg',8.99,5,'2020-11-15 16:57:55','2020-11-15 16:57:55'),
       (25,'White Casual Case','white-casual-case','<p>A simple white casual design glasses case</p>','casual-white-open.jpg',16.99,5,'2020-11-15 16:58:27','2020-11-15 16:58:27'),
       (26,'Perfect Pink','perfect-pink','<p>Perfectly pink glasses case</p>','casual-pink-open.jpg',19.99,6,'2020-11-15 16:58:53','2020-11-15 16:58:53'),
       (27,'Curved Black','curved-black','<p>A lovely design with a curved black case</p>','curve-black-open.jpg',12.99,6,'2020-11-15 16:59:32','2020-11-15 16:59:32'),
       (28,'Black Grey Curved','black-grey-curved','<p>Glasses case with a black grey curved design</p>','curve-blackgrey-open.jpg',14.99,6,'2020-11-15 17:00:31','2020-11-15 17:00:31'),
       (29,'Black Light Blue','black-light-blue','<p>Case with an ocean blue design on black</p>','curve-blacklightblue-open.jpg',23.99,6,'2020-11-15 17:01:05','2020-11-15 17:01:05'),
       (30,'Curved Pink','curved-pink','<p>Curved pink glasses case</p>','curve-blackpink-open.jpg',16.99,6,'2020-11-15 17:01:30','2020-11-15 17:01:30'),
       (31,'Velvet Red','velvet-red','<p>Beautiful glasses case with a curved red design</p>','curve-blackred-open.jpg',18.99,6,'2020-11-15 17:02:05','2020-11-15 17:02:05'),
       (32,'Deep Blue Ocean','deep-blue-ocean','<p>Deep blue ocean glasses case</p>','curve-blue-open.jpg',27.99,6,'2020-11-15 17:02:36','2020-11-15 17:02:36'),
       (33,'Curved Brown','curved-brown','<p>Curved brown glasses case perfect for him or her</p>','curve-brown-open.jpg',19.99,7,'2020-11-25 07:16:45','2020-11-25 07:16:46'),
       (34,'Leopard Skin','leopard-skin','<p>Leopard skin glasses case</p>','curve-brownpattern-open.jpg',22.99,7,'2020-11-15 17:03:42','2020-11-15 17:03:42'),
       (35,'Gold Design','gold-design','<p>Gold design glasses case</p>','curve-gold-open.jpg',39.99,7,'2020-11-15 17:04:13','2020-11-15 17:04:13'),
       (36,'Pink Panther','pink-panther','<p>Pink panther style glasses case</p>','curve-pinkpattern-open.jpg',27.99,7,'2020-11-15 17:04:51','2020-11-15 17:04:51'),
       (37,'Curve Ocean Sky','curve-ocean-sky','<p>Light blue design of the ocean</p>','curve-skyblue-open.jpg',18.99,7,'2020-11-15 17:05:25','2020-11-15 17:05:25'),
       (38,'Plain White','plain-white','<p>Simple plain white glasses case, with blue interior</p>','curve-white-open.jpg',9.99,7,'2020-11-15 17:05:56','2020-11-15 17:05:56'),
       (39,'White Leopard Pattern','white-leopard-pattern','<p>White leopard pattern design glasses case</p>','curve-whitepattern-open.jpg',13.99,7,'2020-11-15 17:06:32','2020-11-15 17:06:32');

INSERT INTO users (id, username, password, email, phone_number)
VALUES (1,'john','pass','john@gmail.com','11111111'),
       (2,'user1','pass','user1@email.com','11111111'),
       (3,'user2','pass','user2@email.com','11111111'),
       (4,'user3','pass','user3@email.com','11111111');
