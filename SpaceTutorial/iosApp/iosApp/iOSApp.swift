import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        KoinHelperKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView(viewModel: .init())
        }
    }
}

struct iOSApp_Previews: PreviewProvider {
    static var previews: some View {
        /*@START_MENU_TOKEN@*/Text("Hello, World!")/*@END_MENU_TOKEN@*/
    }
}
