CREATE DATABASE PRJ301_Assignment_BookShopDB 

USE PRJ301_Assignment_BookShopDB
-- 1.Table: Genre (Bảng thể loại sách)
CREATE TABLE Genre (
    genreID INT IDENTITY(1,1) PRIMARY KEY,
    genreName NVARCHAR(255)
);

-- 2.Table: Author (Bảng chỉ tác giả của sách)
CREATE TABLE Author (
    authorID INT IDENTITY(1,1) PRIMARY KEY,
    authorName NVARCHAR(255) NOT NULL
);

-- 3.Table: Account (Bảng tài khoản người dùng)
--Role: chỉ 0 đối với admin còn 1 đối với người dùng
CREATE TABLE Account (
    accID VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name NVARCHAR(255) NOT NULL,
    role INT CHECK (role IN (0,1)) DEFAULT 0,
    phone VARCHAR(20) UNIQUE,
    isActive BIT DEFAULT 1 -- 1: hoạt động, 0: bị khóa

);

-- 4.Table: Book (Bảng thông tin sách)
CREATE TABLE Book (
    bookID INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    description NVARCHAR(MAX),
    genreID INT,
    authorID INT,
    imageURL VARCHAR(500),
    discount DECIMAL(5,2) CHECK (discount BETWEEN 0 AND 100) DEFAULT 0.0,
    stock INT NOT NULL DEFAULT 0,
    status INT NOT NULL DEFAULT 1,
    FOREIGN KEY (genreID) REFERENCES Genre(genreID) ON DELETE SET NULL,
    FOREIGN KEY (authorID) REFERENCES Author(authorID) ON DELETE SET NULL
);

-- 5.Table: Order (Bảng đơn hàng của người dùng)
-- có 5 trang thái: chờ xử lý, đã xác nhận,đang giao hàng,đã giao hàng thành công, đã hủy.
CREATE TABLE [Order] (
    orderID INT IDENTITY(1,1) PRIMARY KEY,
    userID VARCHAR(255),
    create_at DATETIME DEFAULT GETDATE(),
    status NVARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'confirmed', 'shipped', 'delivered', 'cancelled')),
    totalPrice DECIMAL(10,2) NOT NULL,
    address NVARCHAR(255) NOT NULL, 
    receiver_name NVARCHAR(255) NOT NULL,
    receiver_phone VARCHAR(20) NOT NULL, 
    FOREIGN KEY (userID) REFERENCES Account(accID) ON DELETE CASCADE
);

-- 6.Table: OrderDetail(Bảng chi tiết đơn hàng)
CREATE TABLE OrderDetail (
    orderDetailID INT IDENTITY(1,1) PRIMARY KEY,
    orderID INT,
    bookID INT,
    quantity INT NOT NULL,
    FOREIGN KEY (orderID) REFERENCES [Order](orderID) ON DELETE CASCADE,
    FOREIGN KEY (bookID) REFERENCES Book(bookID) ON DELETE CASCADE
);

-- 7.Table: Cart(Bảng giỏ hàng)
CREATE TABLE Cart (
    cartID INT IDENTITY(1,1) PRIMARY KEY,
    accID VARCHAR(255),
    bookID INT,
    quantity INT NOT NULL,
    FOREIGN KEY (accID) REFERENCES Account(accID) ON DELETE CASCADE,
    FOREIGN KEY (bookID) REFERENCES Book(bookID) ON DELETE CASCADE
);

-- 8.Table: Review (Bảng đánh giá về sách)
CREATE TABLE Review (
    reviewID INT IDENTITY(1,1) PRIMARY KEY,
    bookID INT,
    accID VARCHAR(255),
    comment NVARCHAR(MAX),
    date DATETIME DEFAULT GETDATE(),
    rating INT CHECK (rating BETWEEN 0 AND 5) NOT NULL,
    FOREIGN KEY (bookID) REFERENCES Book(bookID) ON DELETE CASCADE,
    FOREIGN KEY (accID) REFERENCES Account(accID) ON DELETE CASCADE
);
GO 

-- 9.View: BookRating (Bảng này giúp tính trung bình sao của 1 sách đó nó đóng vai trò như 1 bảng ảo)
CREATE VIEW BookRating AS
SELECT
    b.bookID,
    COALESCE(AVG(r.rating), 0) AS rating
FROM Book b
LEFT JOIN Review r ON b.bookID = r.bookID
GROUP BY b.bookID;
GO

--Thêm dữ liệu:

INSERT INTO Genre (genreName)
VALUES 
(N'Truyện Tranh'), 
(N'Tiểu Thuyết'), 
(N'Kinh Dị'), 
(N'Khoa Học'), 
(N'Thiếu Nhi');
GO

INSERT INTO Author (authorName)
VALUES 
(N'Nguyễn Nhật Ánh'), 
(N'J.K. Rowling'), 
(N'Stephen King'), 
(N'Isaac Asimov'), 
(N'Doraemon Team');
GO

INSERT INTO Account (accID, email, password, name, role, phone,isActive)
VALUES
    ('admin1', 'admin@gmail.com', '123', 'admin', 0, '0',1),
    ('user1', 'user@gmail.com', '123', 'User Lee', 1, '0192837465',1),
    ('user2', 'user2@gmail.com', '123', 'User Two Nguyen', 1, '0987654321',1);
GO

INSERT INTO Book (title, price, description, genreID, authorID, imageURL, discount, stock)
VALUES
(N'Mắt Biếc (Tái Bản 2019)', 99500, 
N'Mắt biếc là một tác phẩm được nhiều người bình chọn là hay nhất của nhà văn Nguyễn Nhật Ánh. Tác phẩm này cũng đã được dịch giả Kato Sakae dịch sang tiếng Nhật để giới thiệu với độc giả Nhật Bản. 

“Tôi gửi tình yêu cho mùa hè, nhưng mùa hè không giữ nổi. Mùa hè chỉ biết ra hoa, phượng đỏ sân trường và tiếng ve nỉ non trong lá. Mùa hè ngây ngô, giống như tôi vậy. Nó chẳng làm được những điều tôi ký thác. Nó để Hà Lan đốt tôi, đốt rụi. Trái tim tôi cháy thành tro, rơi vãi trên đường về.”

… Bởi sự trong sáng của một tình cảm, bởi cái kết thúc buồn, rất buồn khi xuyên suốt câu chuyện vẫn là những điều vui, buồn lẫn lộn …  ', 
2, 1, 
'https://salt.tikicdn.com/cache/750x750/ts/product/e2/77/44/170fa32d7932b634a357b4585333582b.jpg.webp', 
10, 50),
(N'Harry Potter and the Philosopher''s Stone', 252860, 
N'Harry Potter thậm chí chưa bao giờ nghe nói về Hogwarts khi những lá thư bắt đầu rơi trên tấm thảm chùi chân ở số bốn, đường Privet Drive. Được ghi địa chỉ bằng mực xanh trên giấy da hơi vàng có đóng dấu màu tím, chúng nhanh chóng bị tịch thu bởi người dì và người chú ghê rợn của anh. Sau đó, vào ngày sinh nhật thứ mười một của Harry, một người đàn ông khổng lồ có đôi mắt bọ cánh cứng tên là Rubeus Hagrid xông vào với một tin tức đáng kinh ngạc: Harry Potter là một phù thủy, và cậu có một suất tại Trường Ma thuật và Pháp thuật Hogwarts. Một cuộc phiêu lưu đáng kinh ngạc sắp bắt đầu! Các ấn bản mới này của bộ truyện cổ điển và bán chạy nhất quốc tế, từng đoạt nhiều giải thưởng có những chiếc áo khoác mới có thể lấy ngay của Jonny Duddle, với sức hấp dẫn lớn đối với trẻ em, để đưa Harry Potter đến với thế hệ độc giả tiếp theo. Đã đến lúc VƯỢT QUA KỲ DIỆU.', 
2, 2, 
'https://salt.tikicdn.com/cache/750x750/ts/product/df/d2/e3/f60721850be6b997a6ed157dc16b294b.jpg.webp', 
5, 40),
(N'The Shining - Thị Kiến', 188300, 
N'Tác giả nói về tác phẩm:

Tôi tin các câu chuyện như thế này tồn tại vì đôi khi ta cần tạo ra những con quái vật và ông kẹ không có thật để thế chỗ cho những điều ta khiếp hãi trong đời thực: người cha/mẹ thay vì yêu thương lại đánh đập ta, vụ tai nạn cướp đi người mà ta yêu quý, khối u ta bỗng một ngày phát hiện ra đang sống trong cơ thể mình. Giả như những sự kiện kinh khủng đó là hành động của một thế lực bóng tối, có lẽ chúng ta sẽ thấy dễ dàng chấp nhận hơn. Nhưng thay vì bóng tối, tôi lại thấy chúng mang trong mình một thứ ánh sáng chói chang, và các hành vi tàn nhẫn trong gia đình mà đôi khi vẫn còn vương lại mãi trong lòng ta, chính là thứ tỏa sáng rực rỡ hơn cả. Nhìn trực tiếp vào thứ ánh sáng chói lòa ấy sẽ khiến ta mù, thế nên ta tạo ra nhiều lớp màn chắn khác nhau. Truyện ma, truyện kinh dị, truyện kỳ bí - thảy đều là những tấm màn như thế. Người nào khăng khăng không có ma trên đời chỉ là đang lờ đi những lời thì thầm của trái tim mình và như thế, đối với tôi, là độc ác. Dĩ nhiên, ngay cả con ma độc ác nhất cũng cô đơn, bị bỏ mặc trong bóng tối, cố gắng để được nghe thấy.',
3, 3, 
'https://salt.tikicdn.com/cache/750x750/ts/product/ab/cc/41/f49b712a9197f1a7833d113b505a676c.jpg.webp',
0, 30),
(N'Foundation (Apple Series Tie-in Edition) by Isaac Asimov (US edition, paperback)', 365600, 
N'The Foundation novels of Isaac Asimov are among the most influential in the history of science fiction, celebrated for their unique blend of breathtaking action, daring ideas, and extensive worldbuilding. In Foundation, Asimov has written a timely and timeless novel of the best--and worst--that lies in humanity, and the power of even a few courageous souls to shine a light in a universe of darkness.',
4, 4,
'https://salt.tikicdn.com/cache/750x750/ts/product/22/11/82/e994563b55c912972cdca75b2070be01.jpg.webp',
15, 20),
(N'Đội Quân Doraemon Tập 1 [Tái Bản 2022]', 19800,
N'Tập truyện là cuộc phiêu lưu đầy thử thách, thú vị của Doremon, Nobita và các bạn. Qua mỗi câu chuyện, người đọc đều cảm thấy trách nhiệm của những nhân vật nhỏ tuổi với trái đất, với môi trường, với thiên nhiên, với việc bảo tồn động vật. Xuyên suốt các tập truyện là sợi chỉ đỏ: Cái thiện sẽ thắng cái ác, sự cố gắng sẽ được đền đáp xứng đáng.

Cuốn sách sẽ đưa chúng ta bước vào thế giới hồn nhiên, trong sáng đầy ắp tiếng cười với một kho bảo bối kì diệu - những bảo bối biến ước mơ của chúng ta thành sự thật. Nhưng trên tất cả Doraemon là hiện thân của tình bạn cao đẹp, của niềm khát khao vươn tới những tầm cao.',
1, 5,
'https://salt.tikicdn.com/cache/750x750/ts/product/44/4d/8b/0abb57d4d6e24d862ae20efc7318f41f.jpg.webp',
0, 100);
GO
