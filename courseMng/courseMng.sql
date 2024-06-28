ALTER TABLE student_mis.admin
    ADD COLUMN is_del INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.course
    ADD COLUMN is_del INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.course_info
    ADD COLUMN is_del INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.profession
    ADD COLUMN is_del INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.silent
    ADD COLUMN is_del INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.student
    ADD COLUMN is_del INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.student_course
    ADD COLUMN is_del INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.teacher
    ADD COLUMN is_del INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.teacher_course
    ADD COLUMN is_del INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.timetable
    ADD COLUMN is_del INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.upload
    ADD COLUMN is_del INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.week
    ADD COLUMN is_del INT NOT NULL DEFAULT 0;

ALTER TABLE student_mis.admin
    ADD COLUMN version INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.course
    ADD COLUMN version INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.course_info
    ADD COLUMN version INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.profession
    ADD COLUMN version INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.silent
    ADD COLUMN version INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.student
    ADD COLUMN version INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.student_course
    ADD COLUMN version INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.teacher
    ADD COLUMN version INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.teacher_course
    ADD COLUMN version INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.timetable
    ADD COLUMN version INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.upload
    ADD COLUMN version INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.week
    ADD COLUMN version INT NOT NULL DEFAULT 0;
ALTER TABLE student_mis.subject
    ADD COLUMN version INT NOT NULL DEFAULT 0;


use student_mis;
-- auto-generated definition
create table subject
(
    id       int auto_increment
        primary key,
    type     varchar(20)   null comment '题型',
    question varchar(100)  null comment '问题',
    answer   varchar(100)  null comment '答案',
    is_del   int default 0 not null
)
    comment '问题';

ALTER TABLE subject
    CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
INSERT INTO subject (type, question, answer, is_del)
VALUES ('选择题', '世界上最大的洲是？A. 亚洲 B. 非洲 C. 欧洲 D. 北美洲', 'A', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('选择题', '以下哪个城市被称为“东方明珠”？A. 北京 B. 上海 C. 广州 D. 深圳', 'B', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('选择题', '下列哪种动物是国家一级保护动物？A. 大熊猫 B. 狮子 C. 斑马 D. 大象', 'A', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('选择题', '《红楼梦》是谁的作品？A. 施耐庵 B. 曹雪芹 C. 罗贯中 D. 吴承恩', 'B', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('填空题', '中国的首都是？', '北京', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('填空题', '太阳系中最大的行星是？', '木星', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('填空题', '《西游记》中的主人公是？', '孙悟空', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('填空题', '地球的自转周期是？', '24小时', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '简述什么是人工智能？',
        '人工智能是一种模拟人类智能思维和行为的技术，通过计算机和其他智能系统来模拟、仿真和实现人类的认知能力和行为，以解决问题和完成任务。',
        0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '什么是气候变化？',
        '气候变化是指地球气候系统的长期变化，通常是由于自然因素和人类活动引起的大气和海洋环境的变化，包括气温、降水、风速等方面的变化。',
        0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '简述互联网的发展历程及其对社会的影响？',
        '互联网的发展历程包括ARPANET、万维网、Web2.0等阶段，它给社会带来了信息的快速传播、信息获取的便利、在线交流互动等方面的影响，推动了全球化、信息化和数字化进程。',
        0);
-- 选择题
INSERT INTO subject (type, question, answer, is_del)
VALUES ('选择题', '地球上的最高峰是？A. 珠穆朗玛峰 B. 喜马拉雅山 C. 安纳普尔那峰 D. 阿尔岭山脉', 'A', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('选择题', '鱼的呼吸器官是？A. 肺 B. 腮 C. 皮肤 D. 皮鳞', 'B', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('选择题', '万里长城的修建始于哪个朝代？A. 明朝 B. 元朝 C. 秦朝 D. 汉朝', 'C', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('选择题', '以下哪个不是原子核中的组成部分？A. 中子 B. 质子 C. 电子 D. 重子', 'C', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('选择题', '太阳的能量主要来源于？A. 氢气 B. 氦气 C. 核聚变 D. 核裂变', 'C', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('选择题', '哪个国家是世界上最大的岛国？A. 澳大利亚 B. 加拿大 C. 印尼 D. 日本', 'C', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('选择题', '《三国演义》是哪位文学家的作品？A. 曹雪芹 B. 施耐庵 C. 吴承恩 D. 罗贯中', 'D', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('选择题', '日本的国花是什么？A. 樱花 B. 菊花 C. 玫瑰 D. 郁金香', 'B', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('选择题', '世界上最深的海沟是？A. 马里亚纳海沟 B. 古巴海沟 C. 巴拿马海沟 D. 马里亚姆海沟', 'A', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('选择题', '麦当劳的招牌产品是？A. 汉堡包 B. 披萨 C. 热狗 D. 炸鸡', 'A', 0);

-- 填空题
INSERT INTO subject (type, question, answer, is_del)
VALUES ('填空题', '长江上游的地方被称为？', '川西', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('填空题', '最大的淡水湖是？', '五大湖', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('填空题', '亚洲最大的岛国是？', '印度尼西亚', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('填空题', '万里长城的修建始于哪个朝代？', '春秋战国', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('填空题', '地球上最大的陆地生物是？', '非洲象', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('填空题', '北美洲最高的山峰是？', '麦金利', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('填空题', '中国古代的六艺不包括？', '琴棋书画', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('填空题', '诗歌体裁中最短的一种是？', '绝句', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('填空题', '我国最长的河流是？', '长江', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('填空题', '世界上最大的岛国是？', '印尼', 0);

-- 简答题
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '世界上最高的山峰是？', '珠穆朗玛峰，位于喜马拉雅山脉，海拔8844.43米。', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '简述中国的长江。',
        '长江，中国第一大河，全长约6300公里，流域面积约180万平方公里，是中国的母亲河，也是世界第三长河。长江流经我国多个省市，对中国的经济、文化和交通起着重要作用。',
        0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '简述鱼的呼吸器官。', '鱼的呼吸器官是腮，通过腮来吸取水中的氧气并排出二氧化碳。', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '什么是万里长城？',
        '万里长城是中国古代的一项伟大工程，始建于春秋战国时期，起初是为了抵御北方游牧民族的侵袭。长城沿山脊、丘陵、河流等地形地势延伸，全长约21,196.18千米，是世界上最长的建筑物之一。',
        0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '什么是原子核？',
        '原子核是原子的中心部分，由质子和中子组成。质子带正电荷，中子不带电荷。原子核带有正电荷，质子和中子的质量大致相等。电子绕着原子核运动。',
        0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '简述太阳的能量来源。',
        '太阳的能量主要来源于核聚变反应。在太阳的核心，氢原子发生核聚变反应，将氢原子聚变成氦原子，释放出巨大的能量，这种过程被称为太阳的“核心”反应。',
        0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '简述印尼是世界上最大的岛国。',
        '印尼位于东南亚，由数千个岛屿组成，总数达到17000多个，因此被称为“印度尼西亚群岛”，是世界上最大的岛国。', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '《三国演义》是哪位文学家的作品？',
        '《三国演义》是中国古代小说，是元末明初小说家罗贯中创作的一部历史题材长篇小说，以三国时期的历史为背景，讲述了东汉末年到三国时期的历史故事。',
        0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '简述日本的国花。',
        '日本的国花是菊花，菊花在日本被视为高贵、高洁的象征，被广泛应用于日本的文化、艺术和传统活动中。', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '简述马里亚纳海沟。',
        '马里亚纳海沟是世界上最深的海沟，位于西太平洋西北部，深度达到约11034米，是地球表面海底地质学的一部分。', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '简述长城的修建历史。',
        '长城的修建始于春秋战国时期，先秦时期的诸侯国为了防御北方的游牧民族，先后兴建了一些长城。到了秦朝，秦始皇统一六国后，修筑了万里长城，起始于今天的甘肃省嘉峪关，止于山海关。',
        0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '简述长江的重要性及其流经的省市。',
        '长江是中国最长的河流，也是中国的母亲河之一，对中国的经济、文化和交通具有重要意义。长江流经的省市包括青海、西藏、四川、云南、重庆、湖北、湖南、江西、安徽、江苏、上海等，覆盖了中国的大部分发达地区。',
        0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '简述鱼的呼吸方式。',
        '鱼类通过腮来呼吸水中的溶解氧，从而获取氧气，并通过皮肤和粘膜吸收水中溶解的氧气，同时释放二氧化碳。这种呼吸方式被称为鳃呼吸和皮肤呼吸。',
        0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '什么是万里长城？',
        '万里长城是中国古代的一项伟大工程，起初是为了防御北方游牧民族的侵袭而修建的。长城东起山海关，西至嘉峪关，全长约21,196.18千米，是世界上最长的建筑物之一，也是中国的标志性建筑之一。',
        0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '什么是原子核？',
        '原子核是原子的中心部分，由质子和中子组成。它带有正电荷，质子和中子的质量大致相等，约为电子质量的1800倍。原子核的直径大约是原子的1/10,000，但其中包含着原子的几乎所有质量。',
        0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '简述太阳的能量来源。',
        '太阳的能量主要来源于核聚变反应。在太阳的核心，氢原子发生核聚变反应，将氢原子聚变成氦原子，释放出巨大的能量，这种过程被称为太阳的“主序”反应。',
        0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '简述印尼是世界上最大的岛国。',
        '印尼是东南亚一个岛国，由于其领土包括17000多个岛屿，因此被称为世界上最大的岛国。印尼位于环太平洋火山带上，拥有丰富的自然资源和多样的文化。',
        0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '《三国演义》是哪位文学家的作品？',
        '《三国演义》是元末明初小说家罗贯中创作的一部历史题材长篇小说，以三国时期的历史为背景，讲述了东汉末年到三国时期的历史故事，被誉为中国古典小说的四大名著之一。',
        0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '简述日本的国花。',
        '日本的国花是菊花，菊花在日本被视为高贵、高洁的象征，被广泛应用于日本的文化、艺术和传统活动中。', 0);
INSERT INTO subject (type, question, answer, is_del)
VALUES ('简答题', '简述马里亚纳海沟。',
        '马里亚纳海沟是世界上最深的海沟，位于西太平洋西北部，深度达到约11034米，是地球表面海底地质学的一部分。', 0);


