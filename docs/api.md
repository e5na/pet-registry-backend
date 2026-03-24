# Overview

## API Endpoint Mapping

### 0. Authentication (`AuthController`)
| Endpoint | Method | Access | Logic |
| :--- | :--- | :--- | :--- |
| `/api/auth/register` | POST | Public | Self-explanatory. |
| `/api/auth/login` | POST | Public | Ditto. |

### 1. Pet Management (`PetController`)
| Endpoint | Method | Access | Logic |
| :--- | :--- | :--- | :--- |
| `/api/pets` | POST | `VET`, `ADMIN` | Vets only registrations. |
| `/api/pets` | GET (All) | `VET`,  `ADMIN` | Privacy: Only Admins see the full registry. |
| `/api/pets/{id}` | GET | `OWNER`, `VET`, `ADMIN` | Owners see their own; Vets see any pet for professional use. |
| `/api/pets/{id}/history` | GET | `OWNER`, `VET`, `ADMIN` | History is available to all roles. |
| `/api/pets/{id}` | PATCH | `OWNER`, `VET`, `ADMIN` | Owners update basic info; Vets update medical status. |
| `.../report-lost` | PATCH | `OWNER` | Same behaviour as the event. |
| `.../mark-found` | PATCH | `SHELTER`, `ADMIN` | Same behaviour as the event. |
| `.../return-to-owner` | PATCH | `SHELTER`, `ADMIN` | Same behaviour as the event. |
| `.../report-death` | PATCH | `OWNER`, `VET`, `ADMIN` | Same behaviour as the event. |
| `.../export` | PATCH | `OWNER`, `ADMIN` | Same behaviour as the event. |
| `/api/pets/{id}` | DELETE | `ADMIN` | Deletion is restricted to Admins. |

### 2. User & Profile Management (`UserController`)
| Endpoint | Method | Access | Logic |
| :--- | :--- | :--- | :--- |
| `/api/users` | POST | `ADMIN` | Only Admins. Public registration is handled by `AuthController`. |
| `/api/users` | GET (All) | `ADMIN` | Only Admins can list all users. |
| `/api/users/{id}` | GET | `Authenticated` | Users see themselves; Admins see anyone. |
| `/api/users/{id}` | PATCH | `Authenticated` | Users update their own data. |
| `/api/users/{id}` | DELETE | `ADMIN` | Sensitive administrative action. |
| `/api/users/{id}/pets` | GET | `OWNER`, `ADMIN` | Users see their own pets. |

### 3. Professional Tools (`MicrochipController`)
| Endpoint | Method | Access | Logic |
| :--- | :--- | :--- | :--- |
| `/api/microchips` | POST | `VET`, `ADMIN` | Only pros/admins register new hardware. |
| `/api/microchips` | GET (All/Search) | `VET`, `ADMIN` | Vets verify chip validity. |
| `/api/microchips/{id}` | GET | `OWNER`, `VET`, `ADMIN` | Lookup is available to all. |
| `/api/microchips/{id}` | PATCH/DELETE | `ADMIN` | Data integrity. |

### 4. Ownership Transfers (`PetTransferController`)
| Endpoint | Method | Access | Logic |
| :--- | :--- | :--- | :--- |
| `/api/pets/{id}/transfers` | POST | `OWNER`, `ADMIN` | Only the current owner can initiate a transfer. |
| `.../accept` | PATCH | `OWNER`, `ADMIN` | Only the recipient can accept. |
| `.../decline` | PATCH | `OWNER`, `ADMIN` | The recipient can decline the offer. |
| `.../cancel` | PATCH | `OWNER`, `ADMIN` | The initiator can revoke the offer. |

### 5. System Metadata (`Species`, `Breed`, `Role`)
| Controller | Method | Access | Logic |
| :--- | :--- | :--- | :--- |
| `Species`/`Breed` | GET | `Authenticated` | Everyone needs the catalog for registration. |
| `Species`/`Breed` | POST/PATCH/DELETE | `ADMIN` | Admins manage the standard catalog. |
| `Role` | ALL | `ADMIN` | Internal role management. |
