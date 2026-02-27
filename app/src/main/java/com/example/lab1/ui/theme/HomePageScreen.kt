package com.example.lab1.ui.theme
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.lab1.RouteA
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.IOException
import kotlin.collections.mutableListOf
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.isDigitsOnly
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError


//Obsługa formularza
open class ItemData {
    var name by mutableStateOf("")
    var quality by mutableStateOf("")
    var category by mutableStateOf("")
    var price by mutableStateOf("")
    var priceBuy by mutableStateOf("")
    var Count by mutableStateOf("1")
}

class SoldItem{
    var name by mutableStateOf("")
    var quality by mutableStateOf("")
    var category by mutableStateOf("")
    var priceBuy by mutableStateOf("")
    var SellPrice by mutableStateOf("")
    var Count by mutableStateOf("1")
    var ID by mutableStateOf("")

}
// Repezentacja danych z DB
data class ItemDatabase(
    val id: String ="",
    val name: String = "",
    val quality: String = "",
    val category: String = "",
    val price: String = "",
    val priceBuy: String = "",
    val Count: String = ""
)


//Główny ekran
@Composable
fun HomePageScreen(navController: NavController,authViewModel: AuthViewModel){
    val authState = authViewModel.authState.observeAsState()
    val state = authState.value

    ItemsTable(authViewModel)
    logoutbutton(authViewModel)


    LaunchedEffect(state) {
        if(state is AuthState.Unauthenticated){
            navController.navigate(RouteA)
        }
    }
}

@Composable
fun logoutbutton(authViewModel: AuthViewModel){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Button(onClick = {
            authViewModel.signout()
            Log.d("Autoryzacja", "${authViewModel.authState}")
        }) {
            Text("Logout")
        }
    }

}
// Pop-up formularz
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePopUpForItemInput(
    onDismissRequest: () -> Unit,
) {
    val QualityItems =
        listOf("Factory New", "Minimal Wear", "Field-Tested", "Well-Worn", "Battle-Scarred")
    val Category = mapOf(1 to "Normal", 2 to "Starttrak")
    val data = remember { ItemData() }
    var isExpanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(QualityItems[0]) }
    var selectedOption by remember { mutableStateOf(1) }
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid
    val scope = rememberCoroutineScope()
    val api = remember { ApiConnect() }
    val Context = LocalContext.current
    var listing by remember { mutableStateOf<Listing?>(null) }



    var selectedItem by remember { mutableStateOf("") }



    val allItems = remember {getItemsName(context = Context)}






    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment =  Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dodaj przedmiot",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    IconButton(onClick = {onDismissRequest()}) {
                        Text("X", fontSize = 15.sp)
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Nazwa przedmiotu", fontSize = 14.sp, fontWeight = FontWeight.Medium)



                    AutoCompleteTextField(
                        suggestions = allItems,
                        onItemSelected = { item ->
                            selectedItem = item
                        },
                        data = data
                    )

                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Wybierz stan przedmiotu")
                    ExposedDropdownMenuBox(
                        expanded = isExpanded,
                        onExpandedChange = { isExpanded = !isExpanded }
                    ) {
                        TextField(
                            modifier = Modifier.menuAnchor(),
                            value = selected,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded) }

                        )

                        ExposedDropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false }) {
                            QualityItems.forEachIndexed { index, text ->
                                DropdownMenuItem(
                                    text = { Text(text = text) },
                                    onClick = {
                                        selected = QualityItems[index]
                                        isExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row() {
                        Category.forEach { (key, value) ->
                            Row(
                                modifier = Modifier
                                    .height(56.dp)
                                    .selectable(
                                        selected = selectedOption == key,
                                        onClick = { selectedOption = key }
                                    )
                            ) {
                                RadioButton(
                                    selected = selectedOption == key,
                                    onClick = { selectedOption = key }
                                )
                                Row(
                                    modifier = Modifier
                                        .padding(10.dp)

                                ) {
                                    Text(text = value)
                                }
                            }
                        }
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row( horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = data.priceBuy, onValueChange = {newPrice ->
                            val formated = newPrice.replace(",",".")
                            data.priceBuy = formated
                        },
                        placeholder = {Text("Cena zakupu")},
                        leadingIcon = { Text("$", color = MaterialTheme.colorScheme.primary) },
                        singleLine = true)
                    OutlinedTextField(
                        modifier = Modifier.weight(0.5f),
                        value = data.Count, onValueChange = {newPrice ->
                            data.Count = newPrice
                        },
                        placeholder = {Text("Ilość")},
                        leadingIcon = { Text("#", color = MaterialTheme.colorScheme.primary) },
                        singleLine = true
                        )
                }
                    }


                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        onClick = {
                        scope.launch {

                            if(!data.Count.isNullOrEmpty() and !data.priceBuy.isNullOrEmpty() and !data.name.isNullOrEmpty()){
                                if(data.priceBuy.toFloatOrNull() != null && data.Count.isDigitsOnly()){
                                    data.quality = selected
                                    data.category = selectedOption.toString()
                                    data.price = api.ConnecToApi(data.name, data.quality,data.category)?.toString() ?: "0.0"
                                    Log.i("Itemy", "Dodano, ${data.name} ${data.quality} ${data.category} ${data.price}")
                                    PushItemtoDataBase("items",data)
                                    Toast.makeText(Context,"Dodano przedmiot",Toast.LENGTH_SHORT).show()
                                    onDismissRequest()
                                } else {
                                    Log.i("Itemy", "Nie wprowadzono poprawnej ceny badz ilosci przedmiotow")
                                    Toast.makeText(Context,"Wprowadz poprawne dane do ilości oraz ceny zakupu",Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Log.i("Itemy", "Niewprowadzono wszystkich danych")
                                Toast.makeText(Context,"Wprowadz wszystkie dane!",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                        Text("Dodaj")
                    }

                    Button(onClick = { onDismissRequest() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                    )
                    {
                        Text("Anuluj")

                    }
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsTable(authViewModel: AuthViewModel){

    val database: DatabaseReference
    val databaseurl = Config.getDbUrl()
    database = Firebase.database(databaseurl).reference
    var items by remember { mutableStateOf<List<ItemDatabase>>(listOf()) }
    var isLoading by remember { mutableStateOf(true)}
    var isRefreshing by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    val userID = auth.currentUser?.uid
    val scope = rememberCoroutineScope()
    val api = remember { ApiConnect() }
    var showSellDialog by remember { mutableStateOf(false) }
    var SoldItem = remember { SoldItem() }

    if (showSellDialog) {
        SellDialoge(SoldItem.name,SoldItem.quality,SoldItem.priceBuy,SoldItem.Count.toInt(),SoldItem.category,SoldItem.ID, onDismissRequest = { showSellDialog = false })
    }

    LaunchedEffect(userID) {
        if (userID != null) {
            database.child("users").child(userID).child("items")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val itemsList = mutableListOf<ItemDatabase>()

                        for (itemSnapshot in snapshot.children) {
                            val item = ItemDatabase(
                                id = itemSnapshot.key ?: "",
                                name = itemSnapshot.child("name").getValue().toString(),
                                quality = itemSnapshot.child("quality").getValue().toString(),
                                category = itemSnapshot.child("category").getValue().toString(),
                                price = itemSnapshot.child("price").getValue().toString(),
                                Count = itemSnapshot.child("count").getValue().toString(),
                                priceBuy = itemSnapshot.child("priceBuy").getValue().toString()
                            )
                            itemsList.add(item)
                        }

                        items = itemsList
                        isLoading = false
                        Log.i("Firebase", "Pobrano ${itemsList.size} przedmiotów")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Firebase", "Błąd: ${error.message}")
                        isLoading = false
                    }
                })
        }
    }


    fun refreshAllPrices() {
        scope.launch {
            isRefreshing = true

            for (item in items) {
                val newPrice = api.RefreshData(item.name, item.quality,item.category)
                if (newPrice != null && userID != null) {
                    // Zaktualizuj cenę w Firebase
                    database.child("users")
                        .child(userID)
                        .child("items")
                        .child(item.id)
                        .child("price")
                        .setValue(newPrice.toString())

                    Log.i("Refresh", "${item.name}: $newPrice USD")
                }

                kotlinx.coroutines.delay(500)
            }

            isRefreshing = false
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 30.dp)
    ) {
        // Nagłówek
        Text(
            text = "Twoje przedmioty",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Podsumowanie
        if (items.isNotEmpty()) {
            val totalValue = items.sumOf { (it.price.toDoubleOrNull() ?: 0.0)  * it.Count.toInt()}
            val totalValueBuy = items.sumOf { (it.priceBuy.toDoubleOrNull()  ?: 0.0) * it.Count.toInt()}


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Liczba przedmiotów: ${items.size}")
                    Text("Wartość: ${"%.2f".format(totalValue)} USD")

                }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),

                horizontalArrangement = Arrangement.Absolute.Right
            ) { Text("Wartość zakupów: ${"%.2f".format(totalValueBuy)} USD")}
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                    .padding(6.dp),

                horizontalArrangement = Arrangement.Absolute.Right
                ) { Text("Podsumowanie: ${"%.2f".format(totalValue - totalValueBuy)} USD", color = if (totalValue - totalValueBuy < 0){Color.Red} else {Color(0xFF228B22)})}
            }
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            items.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Brak przedmiotów. Dodaj pierwszy!")
                    AddItemsButon()
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .heightIn(max = 400.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items) { item ->
                        ItemCard(item = item, onDelete = {
                            database.child("users").child(userID.toString()).child("items").child(item.id).removeValue()
                        }, onSell = {
                            SoldItem.name = item.name
                            SoldItem.quality = item.quality
                            SoldItem.priceBuy = item.priceBuy
                            SoldItem.Count = item.Count
                            SoldItem.category = item.category
                            SoldItem.ID = item.id
                            Log.i("ItemID","Itemcard ${SoldItem.ID}, ${item.id}")

                            showSellDialog = true}



                        )
                    }
                }
                Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top=30.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
                ) {
                    AddItemsButon()
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top=30.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {Button(onClick = { refreshAllPrices() },
                    enabled = !isRefreshing) {
                    Text("Odśwież ceny")
                } }

            }
        }
    }
}

@Composable
fun SellDialoge(
    ItemName: String = "",
    ItemQuality: String = "",
    ItemBuyPrice: String = "",
    ItemCount: Int = 1,
    ItemCategory: String ="",
    ItemID: String ="",
    onDismissRequest: () -> Unit,
){
    var SellPrice by remember { mutableStateOf("") }
    var ItemQuantity by remember { mutableStateOf("1") }
    val api = remember { ApiConnect() }
    var itemImageUrl by remember { mutableStateOf<String?>(null) }
    val Context = LocalContext.current
    val SoldItem = remember { SoldItem() }


    LaunchedEffect(ItemName, ItemQuality) {
        itemImageUrl = api.GetImage(
            ItemName,
            ItemQuality,
        ) as String?
    }

    Log.i("IMG","${itemImageUrl}")

    Dialog(onDismissRequest = {onDismissRequest()}) {
      Card(
          modifier = Modifier
              .fillMaxWidth()
              .wrapContentHeight()
              .padding(16.dp),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.surface)
      ) {
          Column(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(24.dp)
                  .verticalScroll(rememberScrollState()),
              verticalArrangement = Arrangement.spacedBy(20.dp)
          ) {
              Row(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(5.dp)

              ) {
              Text("Sold item")
              }
          }

          HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

          Column(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(24.dp)
          ) {
              Row(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(bottom = 15.dp),
                  horizontalArrangement = Arrangement.spacedBy(16.dp),
                  verticalAlignment = Alignment.CenterVertically,
              ){
                  AsyncImage(
                      model = "https://community.cloudflare.steamstatic.com/economy/image/${itemImageUrl}",
                      contentDescription = null,
                      modifier = Modifier
                          .size(96.dp),
                      onError = {
                          Log.e("COIL_ERROR", "Image load failed", it.result.throwable)
                      }
                  )
                  Column (
                      modifier = Modifier
                          .padding(top = 20.dp),
                      verticalArrangement = Arrangement.spacedBy(3.dp)
                  ) {
                      Text(ItemName, fontWeight = FontWeight.Bold)
                      Text(ItemQuality)
                      if(ItemCategory == "2") {Text("StatTrak™")}
                      Text("Cena zakupu: ${ItemBuyPrice}")
                      Text("Ilość: ${ItemCount}")
                  }
              }

              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

              Column(
                  Modifier.padding(top =15.dp, bottom = 15.dp)
              ) {
                  Text("Ilość sprzedaży")
                  OutlinedTextField(value = ItemQuantity, onValueChange = {newText -> if(newText.isDigitsOnly()){ItemQuantity = newText}}, trailingIcon = {Text("/${ItemCount}")}, modifier = Modifier .width(100.dp), singleLine = true)

                  Text("Cena szt.")
                  OutlinedTextField(value = SellPrice, onValueChange = {newText -> val sanitized = newText.replace(",",".")

                      if(sanitized.isDigitsOnly() || sanitized.matches(Regex("^\\d*[.]?\\d*\$"))){
                          SellPrice = sanitized}}, modifier = Modifier .width(100.dp), singleLine = true)

              }

              HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

              Column (
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(top = 20.dp),
                  verticalArrangement = Arrangement.spacedBy(10.dp)

              ) {
                  Text("Podsumowanie")
                  Text("Przychód: ${"%.2f".format(CalcRevenue(ItemQuantity,SellPrice))}")
                  Text("Koszt:${"%.2f".format( CalcCost(ItemBuyPrice,ItemQuantity))}")
                  Text("Zysk: ${"%.2f".format(CalcProfit(SellPrice,ItemBuyPrice,ItemQuantity))}")
              }
              Row(
                  modifier = Modifier
                      .padding(10.dp)
                      .fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.Center
              ) {
                  Button(onClick = {

                      if (ItemQuantity.toInt() > ItemCount) {
                        Log.e("Selling","User has ${ItemCount} items and want to sell ${ItemQuantity}")
                          Toast.makeText(Context,"Chcesz sprzedać więcej przedmiotów niż posiadasz!",Toast.LENGTH_SHORT).show()
                      } else {
                          SoldItem.name = ItemName
                          SoldItem.quality = ItemQuality
                          SoldItem.priceBuy = ItemBuyPrice
                          SoldItem.SellPrice = SellPrice
                          SoldItem.Count = ItemQuantity

                          Toast.makeText(Context,"Pomyślnie sprzedano!",Toast.LENGTH_SHORT).show()
                          PushItemtoDataBase("SoldItems",SoldItem)
                          EditItemDataBase(ItemCount - ItemQuantity.toInt(),ItemID)
                      }
                  }) {
                      Text("Potwierdź")
                  }
              }
          }
      }
  }
}
fun CalcRevenue(ItemQuantity: String, SellPrice: String): Double {
    val quantity = ItemQuantity.toIntOrNull() ?: 1
    val price = SellPrice.toDoubleOrNull() ?: 0.0
    val revenue = quantity * price
    return revenue
}

fun CalcCost(ItemBuyPrice: String, ItemQuantity: String): Double {
    val quantity = ItemQuantity.toIntOrNull() ?: 1
    val price = ItemBuyPrice.toDoubleOrNull() ?: 0.0
    val cost = quantity * price
    return cost
}
@Composable
fun ItemCard(
    item: ItemDatabase,
    onDelete: () -> Unit,
    onSell: () -> Unit,
) {


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Nazwa
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Stan
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QualityBadge(quality = item.quality)
                    CategoryBadge(category = item.category)
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    ) {
                        Text(
                            text = "${item.Count} szt.",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${"%.2f".format(item.price.toDoubleOrNull() ?: 0.0)} USD",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = ("%.2f USD".format(CalcProfit(item.price,item.priceBuy,item.Count))),
                        color = if (CalcProfit(item.price,item.priceBuy,item.Count) < 0) {
                            Color.Red
                        } else {
                            Color(0xFF228B22)
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

            }

            Column(
                modifier = Modifier
                 .padding(3.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Usuń",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                Row() {
                    IconButton(onClick = onSell) {
                        Icon(
                            imageVector = Icons.Default.Sell,
                            contentDescription = "Sprzedaj",
                            tint = Color(0xFF228B22)
                        )
                    }
                }

            }


        }
    }
}


fun CalcProfit(SellPrice: String, ItemBuyPrice: String, ItemCount: String): Double {
    val SellPrice = SellPrice.toDoubleOrNull() ?: 0.0
    val buyPrice = ItemBuyPrice.toDoubleOrNull() ?: 0.0
    val count = ItemCount.toIntOrNull() ?: 1

    val TotalProfit = (SellPrice  - buyPrice) * count
    return TotalProfit
}
@Composable
fun QualityBadge(quality: String) {
    val color = when (quality) {
        "Factory New" -> MaterialTheme.colorScheme.primary
        "Minimal Wear" -> MaterialTheme.colorScheme.secondary
        "Field-Tested" -> MaterialTheme.colorScheme.tertiary
        "Well-Worn" -> MaterialTheme.colorScheme.error
        "Battle-Scarred" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.outline
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = quality,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 12.sp,
            color = color
        )
    }
}

@Composable
fun CategoryBadge(category: String) {
    val text = if (category == "2") "StatTrak™" else "Normal"
    val color = if (category == "2")
        MaterialTheme.colorScheme.error
    else
        MaterialTheme.colorScheme.outline

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.2f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 12.sp,
            color = color
        )
    }
}

@Composable
fun AddItemsButon(){
    var showDialog by remember {mutableStateOf(false)}
    Button(onClick = {showDialog = true}) {Text("Dodaj item")}
    if(showDialog){
        CreatePopUpForItemInput(
            onDismissRequest = {showDialog = false}
        )
    }
}

fun <T> PushItemtoDataBase(path: String, data: T){
    val database: DatabaseReference
    val databaseurl =Config.getDbUrl()
    database = Firebase.database(databaseurl).reference
    val auth = FirebaseAuth.getInstance()
    val userID = auth.currentUser?.uid

        database.child("users").child(userID.toString()).child(path).push().setValue(data)
}

fun EditItemDataBase(count: Int,itemId: String){
    val database: DatabaseReference
    val databaseurl =Config.getDbUrl()
    database = Firebase.database(databaseurl).reference
    val auth = FirebaseAuth.getInstance()
    val userID = auth.currentUser?.uid

    Log.i("ItemID","${itemId}")

    database.child("users").child(userID.toString()).child("items").child(itemId).child("count").setValue(count)

}

fun getItemsName(context: Context): ArrayList<String> {
    val arr = ArrayList<String>()

    try {
        val inputStream = context.assets.open("items2.json")
        val json = inputStream.bufferedReader().use { it.readText() }

        val jsonArray = JSONArray(json)

        for (i in 0 until jsonArray.length()) {
            arr.add(jsonArray.getString(i))
        }

        Log.i("Pliki", "Załadowano ${arr.size} przedmiotów")

    } catch (e: IOException) {
        Log.e("Pliki", "Błąd: ${e.message}")
    }

    return arr
}



@Composable
fun AutoCompleteTextField(
    suggestions: List<String>,
    onItemSelected: (String) -> Unit,
    data: ItemData,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("")

    }
    var showSuggestions by remember { mutableStateOf(false) }

    val filteredSuggestions = remember(text) {
        if (text.isBlank()) {
            emptyList()
        } else {
            suggestions.filter {
                it.contains(text, ignoreCase = true) && !it.equals(text, ignoreCase = true)
            }
        }
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
                showSuggestions = true
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (showSuggestions && filteredSuggestions.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                LazyColumn {
                    items(filteredSuggestions) { suggestion ->
                        Text(
                            text = suggestion,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    text = suggestion
                                    showSuggestions = false
                                    onItemSelected(suggestion)
                                    data.name = text
                                    Log.i("API","${data.name}")
                                }
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestScreen() {
    SellDialoge("AK-47 | Nightwish","Factory New","10",10) {}
}
