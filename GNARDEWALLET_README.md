# GnardeWallet - Modern Payment App 💳

A cutting-edge mobile payment application built with Kotlin, Jetpack Compose, and Firebase. GnardeWallet features a modern fintech UI with neon blue/purple gradients, glassmorphism design, and an innovative 2-member family approval system for secure high-value transactions.

## 🎯 Features

### Core Payment Features
- **Secure Login & OTP Verification** - Firebase-based authentication
- **Wallet Dashboard** - Modern glassmorphism UI with balance display
- **Send Money** - UPI transfers with fraud detection
- **Receive Money** - Share UPI ID and receive payments
- **QR Code Scanner** - Quick payments via QR scanning
- **Transaction History** - Filter and search past transactions
- **Dark Mode Support** - Built-in dark theme

### Special Features: 2-Member Family Approval System ⭐
- **Add Family Members** - Add up to 2 parents/family members
- **Approval Process**:
  - First member must be approved before adding second
  - PIN verification required for approval
  - Both members must approve high-value transactions (>₹25,000)
- **Approval Notifications** - Real-time status updates
- **Transaction Limits**:
  - Without approvals: Daily limit ₹100,000, Single txn ₹50,000
  - With approvals: Daily limit ₹500,000, Single txn ₹250,000

### Security Features
- **Biometric Authentication** - Fingerprint/Face recognition
- **Device Verification** - Device fingerprinting and trust scoring
- **Fraud Detection** - Real-time fraud scoring and alerts
- **Transaction Limits** - Automatic limits until approvals complete
- **PIN Protection** - 4-digit PIN for sensitive operations
- **Secure Transaction Status** - Color-coded transaction alerts

### UI/UX Design
- **Modern Fintech Theme** - Neon blue and purple gradients
- **Glassmorphism Cards** - Semi-transparent frosted glass effect
- **Smooth Animations** - Video-app style transitions
- **Rounded Modern UI** - 12-24dp border radius on components
- **Responsive Layout** - Adapts to all screen sizes

## 📁 Project Structure

### Data Layer (`app/src/main/java/com/example/tutedude/ecommerce/data/`)

#### Models (`data/model/`)
```
Wallet.kt                    # Wallet account model
Transaction.kt               # Transaction model with status
ApprovedMember.kt           # Family member/parent model
ApprovalRequest.kt          # Approval request model
PaymentModels.kt            # QRTransaction, PaymentNotification, FraudAlert, DeviceInfo
```

#### Database (`data/local/`)
```
PaymentDaos.kt              # All DAO interfaces:
                            # - WalletDao
                            # - TransactionDao
                            # - ApprovedMemberDao
                            # - ApprovalRequestDao
                            # - PaymentNotificationDao
                            # - FraudAlertDao
                            # - DeviceInfoDao

ECommerceDatabase.kt        # Room database configuration (updated)
```

#### Repositories (`data/repository/`)
```
WalletRepository.kt         # Wallet operations (balance, add/deduct funds)
TransactionRepository.kt    # Transaction CRUD and history
ApprovedMemberRepository.kt # Family member management (2-member system)
ApprovalRepository.kt       # Approval request handling
FraudDetectionRepository.kt # Fraud detection and alerts
```

### Presentation Layer (`app/src/main/java/com/example/tutedude/ecommerce/presentation/`)

#### ViewModels (`presentation/*/`)
```
wallet/WalletViewModel.kt                   # Wallet state management
transaction/TransactionViewModel.kt         # Transaction history state
approval/ApprovalViewModel.kt              # Approval system state
```

#### Screens - Wallet (`presentation/wallet/`)
```
WalletDashboardScreen.kt    # Main dashboard with:
                            # - Balance display (glassmorphism)
                            # - Quick action buttons
                            # - Recent transactions
                            # - Neon blue/purple gradients
```

#### Screens - Payment (`presentation/payment/`)
```
SendMoneyScreen.kt          # Send money with:
                            # - UPI input validation
                            # - Transaction fee calculation
                            # - High-value fraud alerts
                            # - Confirmation dialog

ReceiveMoneyScreen.kt       # Receive money with:
                            # - UPI display and copy
                            # - QR code generation
                            # - Payment request creation
                            # - Share options (SMS, Email)

QRScannerScreen.kt          # QR scanner with:
                            # - Camera integration
                            # - ML Kit barcode scanning
                            # - Torch control
                            # - Manual UPI entry fallback
```

#### Screens - Approval (`presentation/approval/`)
```
ApprovedMemberScreen.kt     # Family member management with:
                            # - 2-member approval system
                            # - First member approval requirement
                            # - Member status display
                            # - Add/remove members

ApprovalNotificationScreen.kt # Approval requests with:
                             # - Pending approvals list
                             # - PIN verification
                             # - Accept/Reject actions
                             # - Color-coded status
```

#### Screens - Transaction (`presentation/transaction/`)
```
TransactionHistoryScreen.kt # Transaction history with:
                            # - Filter (All/Sent/Received)
                            # - Search functionality
                            # - Daily stats
                            # - Transaction details
```

#### Screens - Settings (`presentation/settings/`)
```
SettingsScreen.kt           # App settings with:
                            # - Dark mode toggle
                            # - Theme selection
                            # - Security settings
                            # - Notification preferences
                            # - Logout button
```

### Security Layer (`app/src/main/java/com/example/tutedude/ecommerce/security/`)

```
BiometricAuthManager.kt     # Fingerprint/Face authentication
                            # - Check biometric availability
                            # - Prompt user for authentication
                            # - Handle success/failure

TransactionLimitControl.kt  # Transaction limit enforcement
                            # - Daily/Monthly/Single txn limits
                            # - Approval requirement checks
                            # - Remaining balance calculation

DeviceVerificationManager.kt # Device security verification
                             # - Device fingerprinting (SHA-256)
                             # - Root detection
                             # - Trust score calculation
                             # - Device status reporting
```

### Dependency Injection (`app/src/main/java/com/example/tutedude/ecommerce/di/`)

```
RepositoryModule.kt         # Bindings for:
                            # - IWalletRepository
                            # - ITransactionRepository
                            # - IApprovedMemberRepository
                            # - IApprovalRepository
                            # - IFraudDetectionRepository

DatabaseModule.kt           # Provides:
                            # - ECommerceDatabase
                            # - All DAOs
```

### Theme (`app/src/main/java/com/example/tutedude/ecommerce/ui/theme/`)

```
Color.kt                    # Modern fintech colors:
                            # - Neon Blue: #00D4FF
                            # - Neon Purple: #BB86FC
                            # - Neon Green: #00FF88
                            # - Neon Red: #FF3860
                            # - Neon Orange: #FF8800
                            # - Neon Yellow: #FFD700
```

## 🔒 Key Implementation Details

### 2-Member Approval System
1. User adds first family member
2. System requires OTP/email verification
3. First member must approve themselves with PIN
4. Only after first member approval can user add second member
5. Both members must approve transactions above ₹25,000
6. Transaction blocked if either member rejects

### Transaction Flow
1. **User Initiates Transfer**
   - Enter recipient UPI
   - Enter amount
   - System checks fraud score

2. **Fraud Detection**
   - Amount threshold check
   - Location verification
   - Time pattern analysis
   - High-value transaction alert

3. **Approval Check**
   - If amount > ₹25,000 and no approvals: show error
   - If amount > ₹25,000 and has approvals: send approval requests
   - Wait for PIN verification from both members

4. **Transaction Completion**
   - Update wallet balance
   - Save transaction history
   - Send notification

### Security Mechanisms
- **Biometric Locks**: Fingerprint/Face required for sensitive operations
- **PIN Verification**: 4-digit PIN for approval and high-value transactions
- **Device Verification**: Detects rooted devices and restricts operations
- **Fraud Scoring**: ML-based fraud detection (0-100 scale)
- **Transaction Limits**: Enforced until approvals completed

## 🚀 Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Clean Architecture
- **Database**: Room Database (SQLite)
- **Authentication**: Firebase Authentication
- **Cloud Messaging**: Firebase Cloud Messaging
- **ML Kit**: Barcode scanning (QR codes)
- **Biometric**: AndroidX Biometric
- **DI**: Hilt Dependency Injection
- **Networking**: Retrofit + OkHttp

## 📱 Dependencies Added

```gradle
// Compose & UI
androidx.compose:compose-bom:2023.10.01
androidx.compose.material3:material3
androidx.compose.foundation:foundation
androidx.navigation:navigation-compose:2.7.5

// Hilt DI
com.google.dagger:hilt-android:2.48
androidx.hilt:hilt-navigation-compose:1.1.0

// Room Database
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1

// Firebase
com.google.firebase:firebase-auth-ktx
com.google.firebase:firebase-firestore-ktx
com.google.firebase:firebase-messaging-ktx
com.google.firebase:firebase-storage-ktx

// Camera & QR Scanning
androidx.camera:camera-core:1.3.0
androidx.camera:camera-camera2:1.3.0
androidx.camera:camera-lifecycle:1.3.0
androidx.camera:camera-view:1.3.0
com.google.mlkit:barcode-scanning:17.2.0

// Biometric
androidx.biometric:biometric:1.1.0

// Retrofit
com.squareup.retrofit2:retrofit:2.9.0
com.squareup.retrofit2:converter-gson:2.9.0

// Coroutines
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3
org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3
```

## 📋 Implementation Status

### ✅ Completed
- [x] Data models and database layer
- [x] Repository pattern implementation
- [x] ViewModels for state management
- [x] 8 major UI screens with glassmorphism
- [x] 2-member family approval system
- [x] Biometric authentication manager
- [x] Transaction limit control
- [x] Device verification
- [x] Fraud detection repository
- [x] DI module setup
- [x] Modern color theme

### 🔄 In Progress / Pending
- [ ] Firebase Cloud Messaging integration (setup only)
- [ ] Navigation graph update
- [ ] REST API integration for UPI backend
- [ ] OTP verification screens
- [ ] Profile screen
- [ ] Push notification handler
- [ ] Unit and integration tests

## 🛠️ How to Build & Run

1. **Prerequisites**
   - Android Studio Arctic Fox or later
   - Android SDK 24+ (API 24)
   - Firebase project setup

2. **Setup Firebase**
   - Create Firebase project
   - Download `google-services.json`
   - Place in `app/` directory

3. **Build**
   ```bash
   ./gradlew build
   ```

4. **Run**
   ```bash
   ./gradlew installDebug
   ```

## 📝 File Summary

**Total Files Created**: 31+

- **4** Data Models
- **8** DAO interfaces
- **5** Repositories
- **3** ViewModels
- **8** UI Screens
- **3** Security Managers
- **Updated**: 3 DI modules, Color theme

## 🎨 Design Highlights

### Color Palette
- **Primary**: Neon Blue (#00D4FF)
- **Secondary**: Neon Purple (#BB86FC)
- **Success**: Neon Green (#00FF88)
- **Error**: Neon Red (#FF3860)
- **Warning**: Neon Orange (#FF8800) & Yellow (#FFD700)
- **Background**: Dark Glass (#0A0E27, #1A1E3F)

### Glassmorphism Effect
- Semi-transparent backgrounds with `alpha = 0.1f - 0.3f`
- Subtle borders with neon color accents
- 12-24dp rounded corners
- Layered gradient backgrounds

## 🔐 Security Considerations

1. **PINs & Passwords**: Should be hashed (SHA-256 or bcrypt)
2. **API Communication**: Use HTTPS only
3. **Device Fingerprinting**: For fraud prevention
4. **OTP Validation**: Implement server-side verification
5. **Transaction Verification**: Multi-step confirmation required

## 📚 Next Steps

1. Complete Firebase Cloud Messaging setup
2. Integrate UPI backend API
3. Implement OTP screens
4. Add profile/KYC screens
5. Create notification handler service
6. Add unit and integration tests
7. Implement analytics tracking
8. Prepare for production deployment

## 👨‍💼 Author Notes

GnardeWallet is a full-featured payment application demonstrating:
- Modern Android development with Compose
- Clean architecture principles
- Comprehensive security mechanisms
- Family-focused transaction approval system
- Professional fintech UI/UX design

This implementation serves as a foundation for a production-ready payment application with proper API integration, testing, and deployment considerations.

---

**Created**: 2026-05-19  
**App Name**: GnardeWallet  
**Version**: 1.0  
**License**: MIT
