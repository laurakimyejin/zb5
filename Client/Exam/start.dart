import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter/services.dart';
import 'package:file_picker/file_picker.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: LoginPage(),
      routes: {
        '/signup': (context) => SignupPage(),
        '/login': (context) => LoginPage(),
        '/main': (context) => MainPage(),
      },
    );
  }
}

class LoginPage extends StatelessWidget {
  TextEditingController _idController = TextEditingController();
  TextEditingController _passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('로그인 화면'),
      ),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            TextField(
              controller: _idController,
              decoration: InputDecoration(
                labelText: '아이디',
              ),
            ),
            TextField(
              controller: _passwordController,
              obscureText: true,
              decoration: InputDecoration(
                labelText: '비밀번호',
              ),
            ),
            SizedBox(height: 16.0),
            ElevatedButton(
              onPressed: () {
                String id = _idController.text;
                String password = _passwordController.text;

                login(context, id, password);
              },
              child: Text('로그인'),
            ),
            SizedBox(height: 8.0),
            TextButton(
              onPressed: () {
                Navigator.pushNamed(context, '/signup');
              },
              child: Text('회원가입'),
            ),
            SizedBox(height: 8.0),
          ],
        ),
      ),
    );
  }

  void login(BuildContext context, String id, String password) async {
    String url = 'http://182.229.34.184:5502/auth/login';
    Map<String, dynamic> data = {'userName': id, 'password': password};

    try {
      var response = await http.post(
        Uri.parse(url),
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonEncode(data),
      );

      if (response.statusCode == 200) {
        Navigator.pushReplacementNamed(context, '/main', arguments: id);
      } else {
        showDialog(
          context: context,
          builder: (context) => AlertDialog(
            title: Text('로그인 실패'),
            content: Text('아이디 또는 비밀번호가 잘못되었습니다.'),
            actions: [
              TextButton(
                onPressed: () {
                  Navigator.pop(context);
                },
                child: Text('확인'),
              ),
            ],
          ),
        );
      }
    } catch (e) {
      print(e);
      showDialog(
        context: context,
        builder: (context) => AlertDialog(
          title: Text('로그인 오류'),
          content: Text('로그인 요청 중 오류가 발생했습니다.'),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.pop(context);
              },
              child: Text('확인'),
            ),
          ],
        ),
      );
    }
  }
}

class SignupPage extends StatelessWidget {
  TextEditingController _idController = TextEditingController();
  TextEditingController _nameController = TextEditingController();
  TextEditingController _passwordController = TextEditingController();
  TextEditingController _phoneController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('회원가입'),
      ),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            TextField(
              controller: _idController,
              decoration: InputDecoration(
                labelText: '아이디',
              ),
            ),
            TextField(
              controller: _nameController,
              decoration: InputDecoration(
                labelText: '이름',
              ),
            ),
            TextField(
              controller: _passwordController,
              obscureText: true,
              decoration: InputDecoration(
                labelText: '비밀번호',
              ),
            ),
            TextField(
              controller: _phoneController,
              decoration: InputDecoration(
                labelText: '전화번호',
              ),
            ),
            SizedBox(height: 16.0),
            ElevatedButton(
              onPressed: () {
                String id = _idController.text;
                String name = _nameController.text;
                String password = _passwordController.text;
                String phone = _phoneController.text;

                signup(context, id, name, password, phone);
              },
              child: Text('회원가입 완료'),
            ),
          ],
        ),
      ),
    );
  }

  void signup(BuildContext context, String id, String name, String password, String phone) async {
    String url = 'http://182.229.34.184:5502/auth/signup';
    Map<String, dynamic> data = {
      'userName': id,
      'name': name,
      'password': password,
      'phoneNumber': phone,
    };

    try {
      var response = await http.post(
        Uri.parse(url),
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonEncode(data),
      );

      if (response.statusCode == 200) {
        showDialog(
          context: context,
          builder: (context) => AlertDialog(
            title: Text('회원 가입 성공'),
            content: Text('회원 가입이 완료되었습니다.'),
            actions: [
              TextButton(
                onPressed: () {
                  Navigator.pushReplacementNamed(context, '/login');
                },
                child: Text('확인'),
              ),
            ],
          ),
        );
      } else {
        showDialog(
          context: context,
          builder: (context) => AlertDialog(
            title: Text('회원 가입 실패'),
            content: Text('회원 가입에 실패했습니다.'),
            actions: [
              TextButton(
                onPressed: () {
                  Navigator.pop(context);
                },
                child: Text('확인'),
              ),
            ],
          ),
        );
      }
    } catch (e) {
      print(e);
      showDialog(
        context: context,
        builder: (context) => AlertDialog(
          title: Text('오류'),
          content: Text('회원 가입 요청 중 오류가 발생했습니다.'),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.pop(context);
              },
              child: Text('확인'),
            ),
          ],
        ),
      );
    }
  }
}

class MainPage extends StatefulWidget {
  @override
  _MainPageState createState() => _MainPageState();
}

class _MainPageState extends State<MainPage> {
  var _filePath;
  bool _isLoading = false;
  String _phoneNumber = '';
  String userName = '';
  String serverResponse = '';
  String voiceResult = '';

  Future<String> useRootBundle() async {
    return await rootBundle.loadString('assets/text/my_text.txt');
  }

  Future<void> _openFilePicker() async {
    try {
      setState(() {
        _isLoading = true;
      });

      FilePickerResult? result = await FilePicker.platform.pickFiles();

      if (result != null) {
        PlatformFile file = result.files.first;
        setState(() {
          _filePath = file.path!;
        });
      }
    } on PlatformException catch (e) {
      print("지원되지 않는 작업입니다: $e");
    } catch (ex) {
      print(ex);
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  void _showPhoneNumberInput() {
    showDialog(
      context: context,
      builder: (context) {
        TextEditingController _phoneController = TextEditingController();

        return AlertDialog(
          title: Text('전화번호 입력'),
          content: TextField(
            controller: _phoneController,
            decoration: InputDecoration(
              labelText: '전화번호',
            ),
          ),
          actions: [
            TextButton(
              onPressed: () {
                setState(() {
                  _phoneNumber = _phoneController.text;
                });
                Navigator.pop(context);
              },
              child: Text('확인'),
            ),
          ],
        );
      },
    );
  }

  void _getUserName() {
    final args = ModalRoute.of(context)!.settings.arguments;
    if (args != null) {
      setState(() {
        userName = args as String;
      });
    }
  }

  Future<void> _uploadFile() async {
    if (_filePath != null && _phoneNumber.isNotEmpty) {
      String url = 'http://182.229.34.184:5502/api/VoiClaReq';

      setState(() {
        _isLoading = true;
        serverResponse = '';
      });

      try {
        var request = http.MultipartRequest('POST', Uri.parse(url));
        request.fields['declaration'] = _phoneNumber;
        request.fields['userName'] = userName;
        request.files.add(
          await http.MultipartFile.fromPath('file', _filePath),
        );

        var response = await request.send();

        if (response.statusCode == 200) {
          setState(() {
            serverResponse = '파일 전송 성공';
          });

          var responseString = await response.stream.bytesToString();

        } else {
          setState(() {
            serverResponse = '파일 전송 실패';
          });
        }
      } catch (e) {
        print(e);
      } finally {
        setState(() {
          _isLoading = false;
        });
      }
    }
  }

  void _checkServerResponse() async {
    while (true) {
      try {
        String url = 'http://182.229.34.184:5502/api/VoiClaReq';

        var response = await http.get(Uri.parse(url));

        if (response.statusCode == 200) {
          var data = jsonDecode(response.body);
          var result = data['voiceResult'];
          setState(() {
            voiceResult = result;
          });

          if (result == '0') {
            Navigator.push(
              context,
              MaterialPageRoute(builder: (context) => SafetyScreen()),
            );
            break; // 응답을 받으면 반복문을 종료합니다.
          } else if (result == '1') {
            Navigator.push(
              context,
              MaterialPageRoute(builder: (context) => WarningScreen()),
            );
            break; // 응답을 받으면 반복문을 종료합니다.
          }
        }
      } catch (e) {
        print(e);
      }

      await Future.delayed(Duration(seconds: 3));
    }
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    _getUserName();
    _checkServerResponse();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('파일 업로드'),
      ),
      body: _isLoading
          ? LoadingScreen()
          : SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            RichText(
              text: TextSpan(
                text: '제로',
                style: TextStyle(fontSize: 60, color: Colors.black),
                children: [
                  TextSpan(
                    text: '베이스',
                    style: TextStyle(fontSize: 40, color: Colors.red),
                  ),
                ],
              ),
            ),
            SizedBox(height: 40),
            Image.asset(
              'images/icon/check.png',
              width: 200,
              height: 200,
            ),
            SizedBox(height: 40),
            FutureBuilder<String>(
              future: useRootBundle(),
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.done) {
                  if (snapshot.hasData) {
                    return Text(
                      '버전: ${snapshot.data}',
                      style: TextStyle(fontSize: 20),
                    );
                  } else {
                    return Text(
                      '버전을 불러올 수 없습니다.',
                      style: TextStyle(fontSize: 20),
                    );
                  }
                } else {
                  return CircularProgressIndicator();
                }
              },
            ),
            SizedBox(height: 20),
            ElevatedButton(
              child: Text(
                '전화번호 입력',
                style: TextStyle(fontSize: 20),
              ),
              onPressed: _showPhoneNumberInput,
            ),
            SizedBox(height: 20),
            ElevatedButton(
              child: Text(
                '파일 첨부',
                style: TextStyle(fontSize: 20),
              ),
              onPressed: _openFilePicker,
            ),
            SizedBox(height: 20),
            ElevatedButton(
              child: Text(
                '파일 전송',
                style: TextStyle(fontSize: 20),
              ),
              onPressed: _uploadFile,
            ),
            SizedBox(height: 20),
            if (_filePath != null)
              Text(
                '첨부한 파일 경로: $_filePath',
                style: TextStyle(fontSize: 16),
              ),
            if (_phoneNumber.isNotEmpty)
              Text(
                '입력한 전화번호: $_phoneNumber',
                style: TextStyle(fontSize: 16),
              ),
            SizedBox(height: 20),
            Text(
              '서버 응답: $serverResponse',
              style: TextStyle(fontSize: 20),
            ),
            SizedBox(height: 20),
            Text(
              '음성 인식 결과: $voiceResult',
              style: TextStyle(fontSize: 20),
            ),
          ],
        ),
      ),
    );
  }
}

class LoadingScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      height: MediaQuery.of(context).size.height,
      decoration: BoxDecoration(
        gradient: LinearGradient(
          begin: Alignment.topCenter,
          end: Alignment.bottomCenter,
          colors: [
            Colors.blue.shade500,
            Colors.white,
          ],
        ),
      ),
      child: Center(
        child: CircularProgressIndicator(
          color: Colors.black,
        ),
      ),
    );
  }
}

class WarningScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('위험'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Image.asset(
              'images/danger.png',
              width: 200,
              height: 200,
            ),
            SizedBox(height: 20),
            Text(
              '위험 페이지',
              style: TextStyle(fontSize: 24),
            ),
          ],
        ),
      ),
    );
  }
}

class SafetyScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('안전'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Image.asset(
              'images/safety.png',
              width: 200,
              height: 200,
            ),
            SizedBox(height: 20),
            Text(
              '안전 페이지',
              style: TextStyle(fontSize: 24),
            ),
          ],
        ),
      ),
    );
  }
}
