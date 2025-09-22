#!/bin/bash

# Get current IP address
NEW_IP=$(ipconfig getifaddr en0)

if [ -z "$NEW_IP" ]; then
    echo "Error: Could not get IP address from en0 interface"
    exit 1
fi

echo "Updating IP address to: $NEW_IP"

# Paths to the files
NETWORK_CONFIG="/Users/fakhry/Documents/workspace-personal/itenary/itenary-kmm/composeApp/src/androidMain/res/xml/network_security_config.xml"
SHARED_MODULE="/Users/fakhry/Documents/workspace-personal/itenary/itenary-kmm/shared/src/commonMain/kotlin/com/itenary/shared/di/SharedKoinModule.kt"

# Update network_security_config.xml - add new IP if not already present
if ! grep -q "<domain includeSubdomains=\"true\">$NEW_IP</domain>" "$NETWORK_CONFIG"; then
    # Insert the new domain before the closing </domain-config> tag
    sed -i '' "s|    </domain-config>|        <domain includeSubdomains=\"true\">$NEW_IP</domain>\\n    </domain-config>|" "$NETWORK_CONFIG"
    echo "Added $NEW_IP to network_security_config.xml"
else
    echo "$NEW_IP already exists in network_security_config.xml"
fi

# Update SharedKoinModule.kt - remove existing BASE_URL and add the new one
# Remove any existing BASE_URL lines (both commented and uncommented)
sed -i '' "/^\/\/const val BASE_URL/d" "$SHARED_MODULE"
sed -i '' "/^const val BASE_URL/d" "$SHARED_MODULE"

# Add the new BASE_URL after the imports section
sed -i '' "/^import org.koin.dsl.module/a\\
\\
const val BASE_URL = \"http://$NEW_IP:8080\"" "$SHARED_MODULE"

echo "Updated BASE_URL in SharedKoinModule.kt to: http://$NEW_IP:8080"
echo "Done!"
