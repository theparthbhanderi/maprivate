//
//  Logger.swift
//  FixPix
//
//

import Foundation
import os

enum LogType: String {
    case info = "ℹ️ INFO"
    case warning = "⚠️ WARNING"
    case error = "🚨 ERROR"
    case network = "🌐 NETWORK"
    case system = "⚙️ SYSTEM"
    case ai = "🤖 AI"
}

struct Logger {
    static func log(_ message: String, type: LogType = .info, file: String = #file, function: String = #function, line: Int = #line) {
        #if DEBUG
        let fileName = (file as NSString).lastPathComponent
        print("\(type.rawValue) [\(fileName):\(line)] \(message)")
        #endif
    }
    
    static func logNetwork(request: URLRequest) {
        log("Request: \(request.httpMethod ?? "GET") \(request.url?.absoluteString ?? "Unknown")", type: .network)
    }
    
    static func logError(_ error: Error) {
        log("Exception: \(error.localizedDescription)", type: .error)
    }
}
