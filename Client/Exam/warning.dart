import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  Future<String> useRootBundle() async {
    return await rootBundle.loadString('assets/text/warning.txt');
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: WarningScreen(useRootBundle: useRootBundle),
    );
  }
}

class WarningScreen extends StatelessWidget {
  final Future<String> Function() useRootBundle;

  WarningScreen({required this.useRootBundle});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('위험'),
      ),
      body: Column(
        children: [
          Image.asset('images/danger.png'),
          FutureBuilder(
            future: useRootBundle(),
            builder: (context, snapshot) {
              if (snapshot.hasData) {
                return Text('진단: ${snapshot.data}');
              } else {
                return CircularProgressIndicator();
              }
            },
          ),
        ],
      ),
    );
  }
}
