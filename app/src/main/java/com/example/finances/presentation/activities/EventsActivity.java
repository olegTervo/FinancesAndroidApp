package com.example.finances.presentation.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.finances.infrastructure.Database.helpers.DatabaseHelper;
import com.example.finances.infrastructure.Database.helpers.LoanHelper;
import com.example.finances.infrastructure.Database.helpers.OperationHelper;
import com.example.finances.R;
import com.example.finances.presentation.views.MyEasyTable;

public class EventsActivity extends BaseActivity {
    private DatabaseHelper db;

    private int selectedAccount;
    private int selectedType;

    private Object[] operations;
    private MyEasyTable DataTable;

    public EventsActivity() {
        this.db = new DatabaseHelper(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        this.operations = new Object[0];
        this.selectedAccount = 1;
        this.selectedType = 0;

        getData();
        setData();
    }

    public void setEvents(int type) {
        this.selectedType = type;
        getData();
        setEvents();
    }

    public void setAccount(int id) {
        this.selectedAccount = id;
        getData();
        setEvents();
    }

    private void getData() {
        switch (this.selectedType){
            case 0:
                this.operations = OperationHelper.GetAccountOperations(this.db, this.selectedAccount).toArray();
                break;
            case 1:
                this.operations = OperationHelper.GetAccountOperations(this.db, this.selectedAccount).stream().filter(o -> o.amount > 0).toArray();
                break;
            case 2:
                this.operations = OperationHelper.GetAccountOperations(this.db, this.selectedAccount).stream().filter(o -> o.amount < 0).toArray();
                break;
            case 3:
                this.operations = LoanHelper.GetLoans(this.db).toArray();
                break;
            case 4:
                this.operations = OperationHelper.GetAccountOperations(this.db, this.selectedAccount).stream().filter(o -> o.type == 3).toArray();
                break;
            default:
                this.operations = new Object[0];
                break;
        }
    }

    private void setData() {
        Spinner account = findViewById(R.id.Accounts);
        ArrayAdapter<String> items = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ACCOUNTS);
        account.setAdapter(items);
        account.setOnItemSelectedListener(new AccountChangeListener(this));
        account.setSelection(selectedAccount);

        Spinner eventTypes = findViewById(R.id.EventsType);
        ArrayAdapter<String> events = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, EVENT_TYPES);
        eventTypes.setAdapter(events);
        eventTypes.setOnItemSelectedListener(new EventsTypeChangeListener(this));
        eventTypes.setSelection(selectedType);

        getData();
        setEvents();
    }

    private void setEvents(){
        ScrollView eventsListView = findViewById(R.id.investmentsList);
        int tableId = 1101002;

        eventsListView.removeAllViews();
        this.DataTable = new MyEasyTable(this, this.operations, tableId, 1);
        eventsListView.addView(this.DataTable);
    }

    private static final String[] EVENT_TYPES = new String[] {
            "All", "Incomes", "Payments", "Loans", "Investments"
    };

    private static final String[] ACCOUNTS = new String[] {
            "All", "1 - BankAccount", "2 - DailyAccount"
    };
}

class EventsTypeChangeListener implements AdapterView.OnItemSelectedListener {
    private EventsActivity context;

    public EventsTypeChangeListener(EventsActivity context) {
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        context.setEvents(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(context, "No event type selected" , Toast.LENGTH_LONG).show();
    }
}

class AccountChangeListener implements AdapterView.OnItemSelectedListener {
    private EventsActivity context;

    public AccountChangeListener(EventsActivity context) {
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        context.setAccount(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(context, "No account selected" , Toast.LENGTH_LONG).show();
    }
}