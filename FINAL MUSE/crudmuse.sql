-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 11, 2024 at 07:31 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `crudmuse`
--

-- --------------------------------------------------------

--
-- Table structure for table `project`
--

CREATE TABLE `project` (
  `project_id` bigint(20) NOT NULL,
  `project_image` varchar(255) DEFAULT NULL,
  `project_name` varchar(100) NOT NULL,
  `short_description` varchar(100) NOT NULL,
  `long_description` varchar(800) NOT NULL,
  `document_one` varchar(255) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `project`
--

INSERT INTO `project` (`project_id`, `project_image`, `project_name`, `short_description`, `long_description`, `document_one`, `date`, `user_id`) VALUES
(66, 'icecream.png', 'Ice Cream Boy', 'Realism art of a boy eating icecream', 'This piece of realism art was created from a day during the summer where me and my friends were eating icecream and I took a picture of my friend eating their icecream, this picture became the refrence photo for this piece of art, I wanted to challenge myself and make it as realistic as possible!', 'Icecream.png', '2024-11-29', 8),
(67, 'eyes.png', 'All Eyes On You', 'Their eyes are always on you...', 'No matter where you go, no matter how far you run the eyes are always on you. This piece of artwork gives a sense of uneasy in the viewer, the eyes always follow you...', 'eyes.png', '2024-11-26', 32),
(68, 'tlou.png', 'The last of us: Storyboard', 'A storyboard of a selected scene in TLOU', 'This is a storyboard of a scene selected by me and drawn by me for a class on how to properly storyboard a scene, this was one of my favourite scenes in all of the tv show!', 'tlou.png', '2024-11-30', 32),
(69, 'arcane.png', 'Jinx: The revolutionary', 'A sign of change ', 'This is a digital art piece of my favourite Arcane character Jinx, she is a true sign of revolutionary and rebellion within the show and I love how her character was designed, I decided to add my own spin to it and show a side of her we have yet to see in the show - true evil ', 'arcane.png', '2024-11-08', 8),
(70, 'Lemon.jpg', 'Dont Be Sour', 'Only lemons can be sour, not you!', 'This was a painting done by myself during the summer when I was feeling down in the dumps and a friend told me to stop being a sour lemon this being ingrained into my mind gave the idea to paint a bowl of lemons being sour (what they\'re known for) except one who is happy, something we should all try to be.', 'Lemons.jpg', '2024-12-03', 8),
(71, 'Tree.jpg', 'swamplands', 'These lands belong to the swamp, not man.', 'This painting was created after I took a trip to the swamplands and saw how diverse the biome is and how man should never be allowed to touch or change such beauty. It harbours creatures that belong to the lands, not man.', 'Tree.jpg', '2024-12-09', 32),
(83, 'vlad.jpg', 'Vladimir: The Infernal Cleric', 'A haunting depiction of a demonic priest illuminated by an unholy halo of light.', 'This piece of digital art was created for my Halloween animated short story. Vladimir, once a devoted man of God who dedicated his life to aiding those in need, \r\nwas forever changed after an encounter with the sickly old man who dwells in the castle overlooking the valley. This sick figure transformed Vlad into a creature of the night, \r\na predator who now lures seekers of divine light into the abyss, where they face a darkness far beyond human comprehension.', 'vlad.jpg', '2024-11-07', 34),
(84, 'valleys.jpg', 'Whispering Valleys', 'Whispers surround you, leading you to a place where no man should ever tread.', 'This background art was created for my Halloween animated story, serving as the backdrop for the ominous castle that looms over the valley where our main antagonist, Vladimir, begins his descent into darkness. \r\nThe piece took over three days to complete, evolving through multiple iterations—from structured architecture to lighter tones—before settling on a more abstract, watercolor-inspired style. \r\nI felt this approach better captured the dark, brooding atmosphere essential to the narrative.', 'valleys.jpg', '2024-12-06', 34),
(85, 'protector.jpg', 'Emmanuel Valtore', 'Protector of Valle Muerto.', 'This character design was created for my storyboard module, featuring Emmanuel, the eternal protector of the Valley of the Dead. \r\nFor centuries, Emmanuel has served as the valley\'s vigilant guardian, ensuring its dark secrets and supernatural forces remain contained. \r\nWithin this eerie and mysterious valley, strange and otherworldly events occur, threatening to spill beyond its borders. \r\nIt falls to Emmanuel to prevent these disturbances from escaping into the world, preserving the fragile boundary between the living and the dead.', 'protector.jpg', '2024-12-10', 34),
(86, 'charlie.jpg', 'Charlie : Smiling Friend', 'The smiling friends are here to put a smile on your face!', 'Charlie Dompler is a piece of realism art I created as a personal side project. \r\nAs a fan of the animated show Smiling Friends on Adult Swim, I found myself captivated by its unique humor and quirky characters, particularly Charlie. \r\nWhile watching the show, I often imagined how this character might appear if he existed in the real world. \r\nThis curiosity sparked the idea for the project, and I set out to turn Charlie\'s animated features into a realistic interpretation. \r\nThe artwork reflects my attempt to bring a touch of realism to one of my favourite fictional characters while preserving the charm and personality that make him so memorable. \r\nIt’s a fusion of creative exploration and a tribute to a show I love.', 'charlie.jpg', '2024-11-29', 34);

-- --------------------------------------------------------

--
-- Table structure for table `saved_projects`
--

CREATE TABLE `saved_projects` (
  `savedProjectsID` bigint(20) NOT NULL,
  `interestLevel` varchar(10) NOT NULL,
  `notes1` varchar(100) NOT NULL,
  `notes2` varchar(500) NOT NULL,
  `ProjectId` bigint(20) DEFAULT NULL,
  `is_favorite` tinyint(1) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `savedProjectImage` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `showcases`
--

CREATE TABLE `showcases` (
  `showcase_id` bigint(20) NOT NULL,
  `showcase_name` varchar(100) DEFAULT NULL,
  `short_description` varchar(100) NOT NULL,
  `long_description` varchar(400) DEFAULT NULL,
  `status` varchar(50) NOT NULL,
  `submission_deadline` date NOT NULL,
  `showcase_image` varchar(255) DEFAULT NULL,
  `top` varchar(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `showcases`
--

INSERT INTO `showcases` (`showcase_id`, `showcase_name`, `short_description`, `long_description`, `status`, `submission_deadline`, `showcase_image`, `top`) VALUES
(1, 'Character Design', 'Explore a showcase of unique characters, blending creativity, storytelling, and design artistry.', 'Dive into our Character Creation Showcase, a vibrant gallery of imaginative characters crafted with precision and creativity. From bold heroes to whimsical creatures, each design reflects unique personalities and captivating backstories. Perfect for inspiration or admiration, this showcase celebrates the art of character creation across diverse genres and styles.', 'Live', '2024-02-12', 'CharacterDesign.jpeg', 'Yes'),
(2, 'Digital Art', 'Discover stunning digital art showcasing creativity, innovation, and visuals across diverse styles.', 'Explore our Digital Art Showcase, a curated collection of vibrant, innovative works. From surreal landscapes to intricate designs, each piece reflects the boundless creativity of modern digital artists. Celebrate artistry that pushes boundaries, blending imagination and technique to inspire and captivate viewers in every piece. Perfect for creators and enthusiasts alike.', 'Live', '2024-02-21', 'Digital-Art.jpeg', 'Yes'),
(3, 'Logo Creation', 'Explore a showcase of unique logo creations that blend creativity, branding, and design excellence.', 'Discover our Logo Creation Showcase, a curated collection of innovative and impactful logos. Each design reflects creativity, brand identity, and visual appeal, crafted to leave a lasting impression. From minimalist to bold concepts, this showcase celebrates the art of logo design, offering inspiration for businesses, designers, and creatives alike.', 'Live', '2024-02-02', 'LogoCreation.png', 'No'),
(4, 'Painting', 'Explore a painting showcase featuring stunning artworks, from classic to contemporary styles.', 'Discover our Painting Showcase, a curated collection of vibrant and thought-provoking artworks. Featuring a wide range of styles, from timeless classics to modern masterpieces, each painting tells a unique story. This showcase celebrates the beauty of painting, offering inspiration for art lovers and creatives alike with captivating visuals and diverse techniques.', 'Upcoming', '2025-01-03', 'Paintings.jpeg', 'Yes'),
(5, 'Sculptures', 'Explore a showcase of sculptures, highlighting creativity, craftsmanship, and artistic vision.\r\n', 'Discover our Sculpture Showcase, a collection of intricate and inspiring sculptures from various artists. Featuring works in different mediums and styles, from abstract to realistic, each piece demonstrates exceptional craftsmanship and artistic vision. This showcase celebrates the beauty of sculpture, offering inspiration for art lovers, collectors, and creatives alike.', 'Live', '2024-09-01', 'Sculptures.jpeg', 'No'),
(6, 'Sketches / Drawings', 'Explore a showcase of creative sketches, highlighting raw artistry, design, and imagination.', 'Discover our Sketch Showcase, featuring a diverse collection of sketches that capture the essence of creativity. From detailed studies to freehand expressions, each piece reflects the artist\'s unique vision. This showcase celebrates the beauty of sketching, offering inspiration for artists and creatives looking to explore raw, unrefined artistry in its purest form.', 'Live', '2024-11-02', 'Drawing.jpeg', 'No'),
(7, 'Video Editing', 'Explore a showcase of creative video editing, highlighting skill, storytelling, and visual impact.', 'Discover our Video Editing Showcase, featuring a collection of expertly edited videos that highlight creativity and technical skill. From dynamic commercials to emotional short films, each project showcases innovative storytelling and visual impact. This showcase celebrates the art of video editing, offering inspiration for creators and those looking to elevate their content.', 'Upcoming', '2024-12-26', 'Video-Editing.jpeg', 'No'),
(8, 'Virtual Reality Games', 'Explore a showcase of immersive virtual reality games that push boundaries in gameplay and design.', 'Discover our Virtual Reality Games Showcase, featuring cutting-edge VR experiences that redefine gaming. From action-packed adventures to immersive storytelling, each game offers unique worlds and interactive gameplay. This showcase highlights the innovation behind virtual reality, offering inspiration and excitement for gamers and developers alike.', 'Live', '2024-07-22', 'Virtual-Reality.jpeg', 'No'),
(9, 'Websites', 'Explore a showcase of creative websites that combine design, functionality, and innovative features.', 'Discover our Website Showcase, featuring a diverse collection of beautifully crafted websites. Each example highlights innovative design, seamless functionality, and user-centered experiences. From minimalistic layouts to interactive features, this showcase offers inspiration for developers, designers, and digital enthusiasts looking to push creative boundaries.\r\n\r\n\r\n\r\n\r\n\r\n\r\n', 'Live', '2024-08-18', 'Websites.jpeg', 'No');

-- --------------------------------------------------------

--
-- Table structure for table `showcase_project`
--

CREATE TABLE `showcase_project` (
  `id` bigint(20) NOT NULL,
  `project_id` bigint(20) NOT NULL,
  `showcase_id` bigint(20) NOT NULL,
  `approved` varchar(4) DEFAULT ' '
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `showcase_project`
--

INSERT INTO `showcase_project` (`id`, `project_id`, `showcase_id`, `approved`) VALUES
(48, 66, 2, 'Yes'),
(49, 67, 2, 'Yes'),
(50, 68, 1, 'Yes'),
(51, 69, 1, 'Yes'),
(52, 70, 4, 'Yes'),
(53, 71, 4, 'Yes'),
(54, 83, 2, 'Yes'),
(55, 84, 4, 'Yes'),
(56, 85, 1, '  '),
(57, 86, 2, 'Yes');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL,
  `user_type` varchar(50) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `surname` varchar(100) NOT NULL,
  `password` varchar(400) NOT NULL,
  `username` varchar(200) NOT NULL,
  `bio` varchar(100) NOT NULL,
  `user_image` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `user_type`, `email`, `first_name`, `surname`, `password`, `username`, `bio`, `user_image`) VALUES
(8, 'student', 'luke@gmail.com', 'Luke', 'Cummins', 'Student123', 'LukeC', 'My name is Luke Cummins I\'m 3rd year IDM student and I love to design!', 'Student.png'),
(9, 'employer', 'tia@gmail.com', 'Tia', 'Williams', 'Employer123', 'TiaW', 'I am an employee for a recruiting company looking to recruit young artists with talent', 'Employer.png'),
(10, 'admin', 'ailis@gmail.com', 'Ailis', 'Baldwin', 'Admin123', 'AilisB', 'I am the admin of Muse and I oversee any projects that go againsts our communtiy guidelines', 'Admin.png'),
(32, 'student', 'max@gmail.com', 'Max', 'Cummins', 'Student!12', 'Max12', 'I am Max and I love sharing my art work :) ', 'Max.jpg'),
(34, 'student', 'ella@gmail.com', 'Ella', 'Collins', 'Secret123', 'EllaBella', 'Hello! I am Ella and I love creating various artwork.', 'Ella.png'),
(35, 'student', 'q@gmail.com', 'q', 'q', 'Student123', 'q', 'q', 'Otter2.jpg');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`project_id`),
  ADD KEY `FKo06v2e9kuapcugnyhttqa1vpt` (`user_id`);

--
-- Indexes for table `saved_projects`
--
ALTER TABLE `saved_projects`
  ADD PRIMARY KEY (`savedProjectsID`),
  ADD KEY `FKap3d8gn8q294uwft434hn9ysl` (`ProjectId`),
  ADD KEY `FKphce1pj1t004gad7dmplnphku` (`user_id`);

--
-- Indexes for table `showcases`
--
ALTER TABLE `showcases`
  ADD PRIMARY KEY (`showcase_id`);

--
-- Indexes for table `showcase_project`
--
ALTER TABLE `showcase_project`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_showcase` (`showcase_id`),
  ADD KEY `fk_project` (`project_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `project`
--
ALTER TABLE `project`
  MODIFY `project_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=88;

--
-- AUTO_INCREMENT for table `saved_projects`
--
ALTER TABLE `saved_projects`
  MODIFY `savedProjectsID` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `showcases`
--
ALTER TABLE `showcases`
  MODIFY `showcase_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=57;

--
-- AUTO_INCREMENT for table `showcase_project`
--
ALTER TABLE `showcase_project`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `project`
--
ALTER TABLE `project`
  ADD CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `saved_projects`
--
ALTER TABLE `saved_projects`
  ADD CONSTRAINT `FKap3d8gn8q294uwft434hn9ysl` FOREIGN KEY (`ProjectId`) REFERENCES `project` (`project_id`),
  ADD CONSTRAINT `FKphce1pj1t004gad7dmplnphku` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `showcase_project`
--
ALTER TABLE `showcase_project`
  ADD CONSTRAINT `fk_project` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_showcase` FOREIGN KEY (`showcase_id`) REFERENCES `showcases` (`showcase_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
