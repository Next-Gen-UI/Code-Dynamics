class UF {
 public:
  UF(const vector<vector<string>>& accounts) {
    for (const auto& account : accounts)
      for (int i = 1; i < account.size(); ++i) {
        const string& email = account[i];
        id[email] = email;
      }
  }

  void union_(const string& u, const string& v) {
    id[find(u)] = find(v);
  }

  string find(const string& u) {
    return id[u] == u ? u : id[u] = find(id[u]);
  }

 private:
  unordered_map<string, string> id;
};

class Solution {
 public:
  vector<vector<string>> accountsMerge(vector<vector<string>>& accounts) {
    vector<vector<string>> ans;
    unordered_map<string, string> emailToName;
    unordered_map<string, set<string>> idEmailToEmails;
    UF uf(accounts);

    // get {email: name} mapping
    for (const auto& account : accounts)
      for (int i = 1; i < account.size(); ++i)
        if (!emailToName.count(account[i]))
          emailToName[account[i]] = account[0];

    // union emails
    for (const auto& account : accounts)
      for (int i = 2; i < account.size(); ++i)
        uf.union_(account[i], account[i - 1]);

    for (const auto& account : accounts)
      for (int i = 1; i < account.size(); ++i) {
        const string& idEmail = uf.find(account[i]);
        idEmailToEmails[idEmail].insert(account[i]);
      }

    for (const auto& [idEmail, emails] : idEmailToEmails) {
      const string& name = emailToName[idEmail];
      vector<string> row{begin(emails), end(emails)};
      row.insert(begin(row), name);
      ans.push_back(row);
    }

    return ans;
  }
};
