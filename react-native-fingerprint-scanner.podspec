require 'json'
version = JSON.parse(File.read('package.json'))["version"]

Pod::Spec.new do |s|

  s.name            = "react-native-fingerprint-scanner"
  s.version         = version
  s.homepage        = "https://github.com/hieuvp/react-native-fingerprint-scanner"
  s.summary         = "A fingerprint scanner component for react-native"
  s.license         = "MIT"
  s.author          = { "Hieu Van" => "brentvatne@gmail.com",
                        "Gennady Evstratov" => "g@goodworkapps.com" }
  s.platform        = :ios, "8.4"
  s.source          = { :git => "https://github.com/hieuvp/react-native-fingerprint-scanner.git", :tag => "#{s.version}" }
  s.source_files    = 'ios/*.{h,m}'
  s.preserve_paths  = "**/*.js"
  s.framework       = 'LocalAuthentication'

  s.dependency 'React'

end