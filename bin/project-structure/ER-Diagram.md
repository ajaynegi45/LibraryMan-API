[//]: # (# ER Diagram)

[//]: # ()
[//]: # (```mermaid)

[//]: # (erDiagram)

[//]: # (    BOOKS {)

[//]: # (        int book_id PK)

[//]: # (        string title)

[//]: # (        string author)

[//]: # (        string isbn)

[//]: # (        string publisher)

[//]: # (        int published_year)

[//]: # (        string genre)

[//]: # (        int copies_available)

[//]: # (    })

[//]: # ()
[//]: # (    BORROWINGS {)

[//]: # (        int borrowing_id PK)

[//]: # (        int book_id FK)

[//]: # (        int fine_id FK)

[//]: # (        int member_id FK)

[//]: # (        date borrow_date)

[//]: # (        date due_date)

[//]: # (        date return_date)

[//]: # (    })

[//]: # ()
[//]: # (    FINES {)

[//]: # (        int fine_id PK)

[//]: # (        int borrowing_id FK)

[//]: # (        int member_id FK)

[//]: # (        decimal amount)

[//]: # (        boolean paid)

[//]: # (    })

[//]: # ()
[//]: # (    MEMBERS {)

[//]: # (        int member_id PK)

[//]: # (        string name)

[//]: # (        string email)

[//]: # (        string password)

[//]: # (        enum role)

[//]: # (        date membership_date)

[//]: # (    })

[//]: # ()
[//]: # (    NOTIFICATIONS {)

[//]: # (        int notification_id PK)

[//]: # (        int member_id FK)

[//]: # (        string message)

[//]: # (        enum notification_type)

[//]: # (        timestamp sent_date)

[//]: # (        string status)

[//]: # (    })

[//]: # ()
[//]: # (    MEMBERS ||--o{ BORROWINGS : has)

[//]: # (    BOOKS ||--o{ BORROWINGS : has)

[//]: # (    BORROWINGS ||--|{ FINES : generates)

[//]: # (    MEMBERS ||--o{ FINES : receives)

[//]: # (    MEMBERS ||--o{ NOTIFICATIONS : receives)

[//]: # ()
[//]: # (%% ||--o{: One-to-Many relationship.)

[//]: # (%% ||--|{: One-to-One relationship.)

[//]: # ()
[//]: # (```)

<img width="247" alt="Screenshot 2024-08-29 at 12 34 48 AM" src="https://github.com/user-attachments/assets/82d824ca-82fd-4e6b-be85-fd95199664a2">